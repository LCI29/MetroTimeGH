package ru.clementl.metrotimex.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.clementl.metrotimex.converters.toInt
import ru.clementl.metrotimex.converters.toLong
import ru.clementl.metrotimex.converters.toStringCode
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.Shift
import ru.clementl.metrotimex.model.data.WorkDayType
import ru.clementl.metrotimex.model.data.weekDayType
import ru.clementl.metrotimex.repositories.CalendarRepository
import ru.clementl.metrotimex.utils.asSimpleDate
import ru.clementl.metrotimex.utils.logd
import ru.clementl.metrotimex.utils.oddEven
import java.lang.IllegalStateException
import java.time.*

/**
 * View model of fragment for creating/editing shifts
 *
 * Modes: 1000 - creating shift, 2000 - editing shift
 */
class ShiftCreateViewModel(
    val repository: CalendarRepository,
    val mode: Int,
    val editingDay: DayStatus?
) :
    ViewModel() {


    private val uiScope = CoroutineScope(Job() + Dispatchers.Main)

    private var initialStartTime = editingDay?.shift?.startTime ?: LocalTime.of(8, 0)
    private var initialEndTime = editingDay?.shift?.endTime ?: LocalTime.of(16, 0)

    private var _startDate = MutableLiveData<LocalDate>()
    val startDate: LiveData<LocalDate>
        get() = _startDate

    private var _startTime = MutableLiveData<LocalTime>()
    val startTime: LiveData<LocalTime>
        get() = _startTime

    private var _endTime = MutableLiveData<LocalTime>()
    val endTime: LiveData<LocalTime>
        get() = _endTime

    private var _workDayTypeLive = MutableLiveData<WorkDayType>()
    val workDayTypeLive: LiveData<WorkDayType>
        get() = _workDayTypeLive

    var workDayType = workDayTypeLive.value ?: WorkDayType.SHIFT

    init {
        _startTime.value = initialStartTime
        _endTime.value = initialEndTime
        initializeStartDate()
        _workDayTypeLive.value = editingDay?.workDayType ?: WorkDayType.SHIFT
    }

    fun initializeStartDate() {
        uiScope.launch {
            _startDate.value = editingDay?.date ?: getFirstFreeDateFromDb()
        }
    }



    private suspend fun getFirstFreeDateFromDb(): LocalDate {
        val offset = OffsetDateTime.now(ZoneId.systemDefault()).offset
        return withContext(Dispatchers.IO) {
            var epochmilli =
                repository.getLastDateLong()?.div(1000) ?: return@withContext LocalDate.now()
            logd("${epochmilli}, ${LocalDateTime.ofEpochSecond(epochmilli, 0, offset)}")
            var date = LocalDateTime.ofEpochSecond(
                epochmilli, 0, offset
            ).toLocalDate().plusDays(1)
            date
        }
    }


    fun setStartDate(date: LocalDate) {
        _startDate.value = date
    }

    fun setStartTime(time: LocalTime) {
        _startTime.value = time
    }

    fun setEndTime(time: LocalTime) {
        _endTime.value = time
    }

    fun reset() {
        _startTime.value = initialStartTime
        _endTime.value = initialEndTime
    }

    override fun onCleared() {
        super.onCleared()
        logd("ShiftCreateViewModel cleared")
    }

    fun createDay(name: String, startLoc: String, endLoc: String): DayStatus {
        val date = startDate.value ?: throw IllegalStateException("startDate not set")
        val startTime = startTime.value ?: throw IllegalStateException("startTime not set")
        val endTime = endTime.value ?: throw IllegalStateException("endTime not set")
        val wdt = workDayTypeLive.value ?: throw IllegalStateException("workDayType not set")
        val shift = if (workDayTypeLive.value == WorkDayType.SHIFT) {
            Shift(
                name = if (name.isEmpty()) "Смена" else name,
                weekDayTypeString = date.weekDayType().toStringCode(),
                oddEven = date.oddEven(endTime),
                startTimeInt = startTime.toInt(),
                startLoc = startLoc,
                endTimeInt = endTime.toInt(),
                endLoc = endLoc
            )
        } else {
            null
        }
        return DayStatus(date.toLong(), wdt.toInt(), shift)
    }
}

class ShiftCreateViewModelFactory(
    private val repository: CalendarRepository,
    private val mode: Int,
    private val currentDay: DayStatus?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShiftCreateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShiftCreateViewModel(repository, mode, currentDay) as T
        }
        throw IllegalStateException("Unknown ViewModel class")
    }
}