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
import ru.clementl.metrotimex.model.data.Machinist
import ru.clementl.metrotimex.model.data.getCurrentDayStatus
import ru.clementl.metrotimex.model.data.getNextShift
import ru.clementl.metrotimex.model.salary.MachinistSalaryCounter
import ru.clementl.metrotimex.model.states.*
import ru.clementl.metrotimex.repositories.CalendarRepository
import ru.clementl.metrotimex.utils.inFloatHours
import ru.clementl.metrotimex.utils.logd
import ru.clementl.metrotimex.utils.ofPatternTime
import java.lang.Exception
import java.lang.IllegalStateException
import java.time.Duration

class TonightViewModel(private val repository: CalendarRepository, val machinist: Machinist) :
    ViewModel() {

    private val uiScope = CoroutineScope(Job() + Dispatchers.Main)
    private val now: Long
        get() = System.currentTimeMillis()
    private val calendar = mutableListOf<DayStatus>()
    private var isAlive: Boolean = true
    val today: DayStatus?
        get() = now.getCurrentDayStatus(calendar)
    val nextShift: DayStatus?
        get() = now.getNextShift(calendar)
    var counter =
        now.getCurrentDayStatus(calendar)?.let {
            logd("counter: ${today}")
            MachinistSalaryCounter(machinist, it)
        }


    // Current time emitter
    private val currentTime: LiveData<Long> = flow {
        while (isAlive) {
            emit(now)
            delay(UPDATING_DELAY)
        }
    }.asLiveData()

    private val _simpleInterval = Transformations.map(currentTime) {
        it.getInterval(calendar)
    }
    val simpleInterval: LiveData<Interval>
        get() = _simpleInterval

    private val _currentInterval = Transformations.map(currentTime) {
        it.getUnitedInterval(calendar)
    }
    val currentInterval: LiveData<Interval>
        get() = _currentInterval

    private val _simpleState = Transformations.map(currentTime) {
        it.simpleState(calendar)
    }
    val simpleState: LiveData<SimpleState>
        get() = _simpleState

    private val _advancedState = Transformations.map(currentTime) {
        it.advancedState(calendar)
    }
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
        (endPointMilli - currentMilli).coerceAtLeast(0).ofPatternTime()
    }

    val progress: LiveData<Int> = Transformations.map(currentTime) { _currentMilli ->
        val currentMilli = _currentMilli ?: 0
        val duration = currentInterval.value?.duration ?: Long.MAX_VALUE
        val startPointMilli = _currentInterval.value?.startPoint?.milli ?: Long.MIN_VALUE
        val gone = currentMilli - startPointMilli

        val progress = ((gone.toDouble() / duration.toDouble()) * 100).toInt()
        progress
    }

    val percentProgress: LiveData<String> = Transformations.map(currentTime) { _currentMilli ->
        val currentMilli = _currentMilli ?: 0
        val duration = currentInterval.value?.duration ?: Long.MAX_VALUE
        val startPointMilli = _currentInterval.value?.startPoint?.milli ?: Long.MIN_VALUE
        val gone = currentMilli - startPointMilli

        val progress = (gone.toDouble() / duration.toDouble()) * 100
        if (currentInterval.value?.simpleState == ShiftSimpleState) {
            String.format("%.2f%%", progress)
        } else {
            String.format("%.2f%%", 100 - progress)
        }
    }

    val duration: LiveData<String> = Transformations.map(currentInterval) { interval ->
        val iDuration = interval.duration ?: return@map "--"
        val duration = Duration.ofMillis(iDuration)
        duration.inFloatHours()
    }

    val currentSalary: LiveData<String> =
        Transformations.map(currentTime) { now ->
            if (counter == null) {
                now.getCurrentDayStatus(calendar)?.let {
                    counter = MachinistSalaryCounter(machinist, it)
                }
            }
            counter?.let { counter ->
                now?.let { time ->
                    logd("time = $time, salary = ${counter.getSalary(time)}")
                    String.format("%.2f Р", counter.getSalary(time))
                }
            }

        }


    init {
        logd("TonightViewModel start initialization")
        initialize()

    }

    private fun initialize() {
        uiScope.launch {
            calendar.addAll(loadDays(DAYS_FOR_TONIGHT_BEFORE, DAYS_FOR_TONIGHT_AFTER))
            initializeCurrentInterval()
            initializeSimpleState()
            initializeAdvancedState()
        }
    }

    private fun initializeSimpleState() {
//        _simpleState.value = now.simpleState(getCalendar())
//        logd("ViewModel.initializeStatus(): currents daysList size = ${calendar.size}, currentStatus = ${simpleState.value?.desc}")
    }

    private fun initializeAdvancedState() {
//        _advancedState.value = now.advancedState(getCalendar())
//        logd("Current Advanced State = ${advancedState.value}")
    }

    private fun initializeCurrentInterval() {
//        _currentInterval.value = now.getUnitedInterval(calendar)
    }

    private fun initializeSimpleInterval() {
//        _simpleInterval.value = now.getInterval(calendar)
    }


    fun getCalendar(): List<DayStatus> = calendar


    private fun updateValues() {
        val milli = currentTime.value ?: throw Exception("current time Null value")

        // Assign liveData fields to currentTime LiveData

    }

    private fun updateIntervalAndState() {
        initializeSimpleInterval()
        initializeCurrentInterval()
        initializeSimpleState()
        initializeAdvancedState()
    }

    /**
     * Returns List of DayStatus with given days count before and after current day
     */
    private suspend fun loadDays(daysBeforeCount: Int, daysAfterCount: Int): List<DayStatus> {
        val dayId = now.toDate().toLong()
        return withContext(Dispatchers.IO) {
            val list = mutableListOf<DayStatus>()
            coroutineScope {
                list.addAll(repository.loadDaysBefore(dayId, daysBeforeCount))
            }
            coroutineScope {
                list.addAll(repository.loadDaysAfterAndThis(dayId, daysAfterCount))
            }
            list
        }
    }

    override fun onCleared() {
        super.onCleared()
        logd("TonightViewModel: onCleared()")
        isAlive = false // stops emitting time
    }
}


class TonightViewModelFactory(
    private val repository: CalendarRepository,
    val machinist: Machinist
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TonightViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TonightViewModel(repository, machinist) as T
        }
        throw IllegalStateException("Unknown ViewModel class")
    }
}
