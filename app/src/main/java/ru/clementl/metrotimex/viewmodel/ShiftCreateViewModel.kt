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

    private var _endDate = MutableLiveData<LocalDate>()
    val endDate: LiveData<LocalDate>
        get() = _endDate

    private var _startTime = MutableLiveData<LocalTime>()
    val startTime: LiveData<LocalTime>
        get() = _startTime

    private var _endTime = MutableLiveData<LocalTime>()
    val endTime: LiveData<LocalTime>
        get() = _endTime

    private var _workDayTypeLive = MutableLiveData<WorkDayType>()
    val workDayTypeLive: LiveData<WorkDayType>
        get() = _workDayTypeLive

    private var _isReserveLive = MutableLiveData<Boolean>()
    val isReserveLive: LiveData<Boolean>
        get() = _isReserveLive

    private var _hasAtzLive = MutableLiveData<Boolean>()
    val hasAtzLive: LiveData<Boolean>
        get() = _hasAtzLive

    private var _hasTechLive = MutableLiveData<Boolean>()
    val hasTechLive: LiveData<Boolean>
        get() = _hasTechLive

    private val _startLocsList = MutableLiveData<List<String>>(listOf())
    val startLocList: LiveData<List<String>>
        get() = _startLocsList

    private val _endLocsList = MutableLiveData<List<String>>(listOf())
    val endLocList: LiveData<List<String>>
        get() = _endLocsList


    init {
        _startTime.value = initialStartTime
        _endTime.value = initialEndTime
        initializeStartAndEndDate()
        _workDayTypeLive.value = editingDay?.workDayType ?: WorkDayType.SHIFT
        _isReserveLive.value = editingDay?.shift?.isReserve ?: false
        _hasAtzLive.value = editingDay?.shift?.hasAtz ?: false
        _hasTechLive.value = editingDay?.hasTechUch ?: false
        initializeLocLists()
    }

    private fun initializeLocLists() {
        viewModelScope.launch {
            _startLocsList.value = repository.getStartLocations()
            _endLocsList.value = repository.getEndLocations()
        }
    }

    fun initializeStartAndEndDate() {
        uiScope.launch {
            _startDate.value = editingDay?.date ?: getFirstFreeDateFromDb()
            _endDate.value = startDate.value?.plusDays(1)
        }
    }


    private suspend fun getFirstFreeDateFromDb(): LocalDate {
        val offset = OffsetDateTime.now(ZoneId.systemDefault()).offset
        return withContext(Dispatchers.IO) {
            val epochmilli =
                repository.getLastDateLong()?.div(1000) ?: return@withContext LocalDate.now()
            logd("${epochmilli}, ${LocalDateTime.ofEpochSecond(epochmilli, 0, offset)}")
            val date = LocalDateTime.ofEpochSecond(
                epochmilli, 0, offset
            ).toLocalDate().plusDays(1)
            date
        }
    }


    fun setStartDate(date: LocalDate) {
        _startDate.value = date
    }

    fun setEndDate(date: LocalDate) {
        _endDate.value = date
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

    fun onReserveChecked(isChecked: Boolean) {
        _isReserveLive.value = isChecked
    }

    fun onAtzChecked(isChecked: Boolean) {
        _hasAtzLive.value = isChecked
    }

    fun onTechChecked(isChecked: Boolean) {
        _hasTechLive.value = isChecked
    }

    override fun onCleared() {
        super.onCleared()
        logd("ShiftCreateViewModel cleared")
    }

    fun createDay(name: String, startLoc: String, endLoc: String, notes: String?): DayStatus {
        val date = startDate.value ?: throw IllegalStateException("startDate not set")
        val startTime = startTime.value ?: throw IllegalStateException("startTime not set")
        val endTime = endTime.value ?: throw IllegalStateException("endTime not set")
        val wdt = workDayTypeLive.value ?: throw IllegalStateException("workDayType not set")
        val isReserve = isReserveLive.value ?: throw IllegalStateException("isReserve not set")
        val hasAtz = hasAtzLive.value ?: throw IllegalStateException("hasAtz not set")
        val hasTech = hasTechLive.value ?: throw IllegalStateException("hasTech not set")
        val shift = if (workDayTypeLive.value == WorkDayType.SHIFT) {
            Shift(
                name = if (name.isEmpty()) "Смена" else name,
                weekDayTypeString = date.weekDayType().toStringCode(),
                oddEven = date.oddEven(endTime),
                startTimeInt = startTime.toInt(),
                startLoc = startLoc,
                endTimeInt = endTime.toInt(),
                endLoc = endLoc,
                isReserveInt = if (isReserve) 1 else 0,
                hasAtzInt = if (hasAtz) 1 else 0
            )
        } else {
            null
        }
        return DayStatus(
            date.toLong(), wdt.toInt(), shift, notes,
            hasTechUchInt = if (hasTech) 1 else 0
        )
    }

    fun createDaysInRange(): List<DayStatus> {
        val list = mutableListOf<DayStatus>()
        val wdt = workDayTypeLive.value ?: return list
        if (wdt !in setOf(WorkDayType.VACATION_DAY, WorkDayType.SICK_LIST)) return list
        val dateFrom = startDate.value ?: throw IllegalStateException("startDate not set")
        val dateTo = endDate.value ?: throw IllegalStateException("startDate not set")
        var d = LocalDate.from(dateFrom)
        while (!d.isAfter(dateTo)) {
            list.add(DayStatus(d.toLong(), wdt.toInt(), null))
            d = d.plusDays(1)
        }
        logd(
            """
            LIST = $list
        """.trimIndent()
        )
        return list
    }

    fun onWorkDayTypeChanged(workDayType: WorkDayType) {
        _workDayTypeLive.value = workDayType
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