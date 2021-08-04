package ru.clementl.metrotimex.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.clementl.metrotimex.DAYS_FOR_TONIGHT_AFTER
import ru.clementl.metrotimex.DAYS_FOR_TONIGHT_BEFORE
import ru.clementl.metrotimex.converters.toLong
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.states.SimpleState
import ru.clementl.metrotimex.model.states.simpleState
import ru.clementl.metrotimex.repositories.CalendarRepository
import ru.clementl.metrotimex.utils.logd
import java.lang.IllegalStateException
import java.time.LocalDateTime

class TonightViewModel(private val repository: CalendarRepository) : ViewModel() {

    private val uiScope = CoroutineScope(Job() + Dispatchers.Main)
    private val tonight: LocalDateTime
        get() = LocalDateTime.now()
    private val daysList = mutableListOf<DayStatus>()
    private var isAlive: Boolean = true

    init {
        logd("TonightViewModel start initialization")
        initialize()
    }

    private val _simpleState = MutableLiveData<SimpleState>()
    val simpleState: LiveData<SimpleState>
        get() = _simpleState

    val timeGone = initializeTimeGone()

    private val _timeTill = MutableLiveData<Long>()
    val timeTill: LiveData<Long>
        get() = _timeTill



    private fun initialize() {
        uiScope.launch {
            daysList.addAll(loadDays(DAYS_FOR_TONIGHT_BEFORE, DAYS_FOR_TONIGHT_AFTER))
            initializeStatus()
            initializeTimeGone()
//            initializeTimeTill()
        }
    }

    private fun initializeTimeGone(): LiveData<String> {
        return flow {
            while (isAlive) {
                emit(System.currentTimeMillis().toString())
                delay(100L)
            }
        }.asLiveData()
    }

    fun initializeStatus() {
        _simpleState.value = System.currentTimeMillis().simpleState(getDaysList())
        logd("ViewModel.initializeStatus(): currents daysList size = ${daysList.size}, currentStatus = ${simpleState.value?.desc}")
    }


    private suspend fun loadDays(daysBeforeCount: Int, daysAfterCount: Int): List<DayStatus> {
        logd("loadDays() start")
        val dayId = tonight.toLocalDate().toLong()
        return withContext(Dispatchers.IO) {
            val list = mutableListOf<DayStatus>()
            coroutineScope {
                logd("daysBefore coroutine start")
                list.addAll(repository.loadDaysBefore(dayId, daysBeforeCount))
            }
            coroutineScope {
                logd("daysAfter coroutine start")
                list.addAll(repository.loadDaysAfterAndThis(dayId, daysAfterCount))
            }
            logd("$list")
            list
        }
    }

    fun getDaysList(): List<DayStatus> = daysList



    override fun onCleared() {
        super.onCleared()
        isAlive = false
    }
}

class TonightViewModelFactory(private val repository: CalendarRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TonightViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TonightViewModel(repository) as T
        }
        throw IllegalStateException("Unknown ViewModel class")
    }
}