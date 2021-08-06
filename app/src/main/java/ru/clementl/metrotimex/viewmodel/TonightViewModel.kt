package ru.clementl.metrotimex.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import ru.clementl.metrotimex.DAYS_FOR_TONIGHT_AFTER
import ru.clementl.metrotimex.DAYS_FOR_TONIGHT_BEFORE
import ru.clementl.metrotimex.UPDATING_DELAY
import ru.clementl.metrotimex.converters.toDate
import ru.clementl.metrotimex.converters.toLong
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.states.*
import ru.clementl.metrotimex.repositories.CalendarRepository
import ru.clementl.metrotimex.utils.inFloatHours
import ru.clementl.metrotimex.utils.logd
import ru.clementl.metrotimex.utils.ofPatternTime
import java.lang.Exception
import java.lang.IllegalStateException
import java.time.Duration

class TonightViewModel(private val repository: CalendarRepository) : ViewModel() {

    private val uiScope = CoroutineScope(Job() + Dispatchers.Main)
    private val now: Long
        get() = System.currentTimeMillis()
    private val calendar = mutableListOf<DayStatus>()
    private var isAlive: Boolean = true


    // Current time emitter
    private val currentTime: LiveData<Long> = flow {
        while (isAlive) {
            emit(now)
            currentInterval.value?.endPoint?.milli?.let {
                if (now > it) updateIntervalAndState()
            }
            delay(UPDATING_DELAY)
        }
    }.asLiveData()

    private val _currentInterval = MutableLiveData<Interval>()
    val currentInterval: LiveData<Interval>
        get() = _currentInterval

    private val _simpleState = MutableLiveData<SimpleState>()
    val simpleState: LiveData<SimpleState>
        get() = _simpleState

    private val _advancedState = MutableLiveData<AdvancedState>()
    val advancedState: LiveData<AdvancedState>
        get() = _advancedState

    val timeGone: LiveData<String> = Transformations.map(currentTime) { _currentMilli ->
        val currentMilli = _currentMilli ?: return@map "..."
        val startPointMilli = _currentInterval.value?.startPoint?.milli ?: return@map "..."
        (currentMilli - startPointMilli).ofPatternTime()
    }

    val timeLeft: LiveData<String> = Transformations.map(currentTime) { _currentMilli ->
        val currentMilli = _currentMilli ?: return@map "..."
        val endPointMilli = _currentInterval.value?.endPoint?.milli ?: return@map "..."
        (endPointMilli - currentMilli).ofPatternTime()
    }

    val progress: LiveData<Int> = Transformations.map(currentTime) { _currentMilli ->
        val currentMilli = _currentMilli ?: 0
        val duration = currentInterval.value?.duration ?: Long.MAX_VALUE
        val startPointMilli = _currentInterval.value?.startPoint?.milli ?: Long.MIN_VALUE
        val gone = currentMilli - startPointMilli

        val progress = ((gone.toDouble() / duration.toDouble()) * 100).toInt()
        progress
    }

    val duration: LiveData<String> = Transformations.map(currentInterval) { interval ->
        val iDuration = interval.duration ?: return@map "--"
        val duration = Duration.ofMillis(iDuration)
        duration.inFloatHours()
    }

    init {
        logd("TonightViewModel start initialization")
        initialize()

    }

    private fun initialize() {
        uiScope.launch {
            calendar.addAll(loadDays(DAYS_FOR_TONIGHT_BEFORE, DAYS_FOR_TONIGHT_AFTER))
            initializeSimpleState()
            initializeAdvancedState()
            initializeCurrentInterval()
        }
    }

    private fun initializeSimpleState() {
        _simpleState.value = now.simpleState(getCalendar())
        logd("ViewModel.initializeStatus(): currents daysList size = ${calendar.size}, currentStatus = ${simpleState.value?.desc}")
    }

    private fun initializeAdvancedState() {
        _advancedState.value = now.advancedState(getCalendar())
    }

    private fun initializeCurrentInterval() {
        _currentInterval.value = now.getInterval(calendar)
    }




    fun getCalendar(): List<DayStatus> = calendar


    private fun updateValues() {
        val milli = currentTime.value ?: throw Exception("current time Null value")

        // Assign liveData fields to currentTime LiveData

    }

    private fun updateIntervalAndState() {
        initializeCurrentInterval()
        initializeSimpleState()
        initializeAdvancedState()
    }

    /**
     * Returns List of DayStatus with given days count before and after current day
     */
    private suspend fun loadDays(daysBeforeCount: Int, daysAfterCount: Int): List<DayStatus> {
        logd("loadDays() start")
        val dayId = now.toDate().toLong()
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

    override fun onCleared() {
        super.onCleared()
        isAlive = false // stops emitting time
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