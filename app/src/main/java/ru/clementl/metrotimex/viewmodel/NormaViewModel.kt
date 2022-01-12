package ru.clementl.metrotimex.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.clementl.metrotimex.converters.toLong
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.MachinistStatus
import ru.clementl.metrotimex.model.data.WorkDayType
import ru.clementl.metrotimex.model.norma.WorkMonth
import ru.clementl.metrotimex.model.norma.YearMonthData
import ru.clementl.metrotimex.model.salary.WorkMonthSalaryCounter
import ru.clementl.metrotimex.repositories.YearMonthRepository
import ru.clementl.metrotimex.utils.*
import java.lang.Exception
import java.lang.IllegalStateException
import java.time.Duration
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt
import kotlin.time.toDuration

class NormaViewModel(
    val calendar: List<DayStatus>,
    val statusList: LiveData<List<MachinistStatus>>,
    val yearMonthDataList: LiveData<List<YearMonthData>>,
    val yearMonthRepository: YearMonthRepository,
    val yearMonth: YearMonth,
    val fiveDayWeek: Boolean
) : ViewModel() {

    private val uiScope = CoroutineScope(Job() + Dispatchers.Main)

    private val _currentMonth = MutableLiveData<WorkMonth>(
        initializeWorkMonth()
    )
    val currentMonth: LiveData<WorkMonth>
        get() = _currentMonth

    val totalHoursString: LiveData<String> = Transformations.map(currentMonth) {
        it.workedInMillis(withoutHolidays = false).inFloatHours()
    }

    val weekendString: LiveData<String> = Transformations.map(currentMonth) {
        it.weekendString
    }

    val normaString: LiveData<String> = Transformations.map(currentMonth) {
        it.normaString
    }

    val workDayString: LiveData<String> = Transformations.map(currentMonth) {
        it.workdayString
    }

    val holidaysString: LiveData<String> = Transformations.map(currentMonth) {
        it.holidayMillis.inFloatHours()
    }

    val overworkString: LiveData<String> = Transformations.map(currentMonth) {
        it.overworkMillis.inFloatHours()
    }

    val sickListDaysString: LiveData<String> = Transformations.map(currentMonth) {
        it.countOf(WorkDayType.SICK_LIST, WorkDayType.SICK_LIST_CHILD).toString()
    }

    val vacationDaysString: LiveData<String> = Transformations.map(currentMonth) {
        it.countOf(WorkDayType.VACATION_DAY, WorkDayType.STUDY_DAY).toString()
    }

    val nightShiftsString: LiveData<String> = Transformations.map(currentMonth) {
        it.nightShifts.toString()
    }

    val eveningShiftsString: LiveData<String> = Transformations.map(currentMonth) {
        it.eveningShiftsCount.toString()
    }

    val nightHoursString: LiveData<String> = Transformations.map(currentMonth) {
        Duration.ofMillis(it.nightMillis).inFloatHours(false)
    }

    val eveningHoursString: LiveData<String> = Transformations.map(currentMonth) {
        Duration.ofMillis(it.eveningMillis).inFloatHours(false)
    }

    val masterHoursString: LiveData<String> = Transformations.map(currentMonth) {
        Duration.ofMillis(it.asMasterMillis).inFloatHours()
    }

    val mentorHoursString: LiveData<String> = Transformations.map(currentMonth) {
        Duration.ofMillis(it.asMentorMillis).inFloatHours()
    }

    private val counter: LiveData<WorkMonthSalaryCounter> = Transformations.map(currentMonth) {
        WorkMonthSalaryCounter(it)
    }

    private val totalSalary: LiveData<Double> = Transformations.map(counter) {
        it.getSalary(LocalDateTime.now().toLong())
    }

    val totalSalaryString: LiveData<String> = Transformations.map(totalSalary) {
        it.salaryStyle()
    }

    val lineHoursString: LiveData<String> = Transformations.map(currentMonth) {
        Duration.ofMillis(it.baseLineTimeMillis).inFloatHours()
    }

    val reserveHoursString: LiveData<String> = Transformations.map(currentMonth) {
        Duration.ofMillis(it.baseReserveTimeMillis).inFloatHours()
    }

    val progressPercentageString: LiveData<String> = Transformations.map(currentMonth) {
        it.progressString
    }

    val wasTechUch: LiveData<Boolean> = Transformations.map(currentMonth) {
        it.wasTechUch
    }

    val avgRateValueString: LiveData<String> = Transformations.map(counter) {
        var avg = it.getSalaryWithoutVacation(LocalDateTime.now().toLong()) /
                it.workMonth.workedInHoursTotal
        logd("avg = $avg")
        if (avg == Double.POSITIVE_INFINITY || avg.isNaN()) avg = 0.0
        avg.salaryStyle()
    }

    // For changing premia value
    val premia: LiveData<String> = Transformations.map(yearMonthDataList) {
        _currentMonth.value = initializeWorkMonth()
        "${it.find { yearMonthData -> yearMonthData.yearMonth == currentMonth.value?.yearMonth }?.premia?.toInt()}%"
    }
    // For changing month
    val premia2: LiveData<String> = Transformations.map(currentMonth) {
        "${it.premia.times(100).toInt()}%"
    }

    val premiaField: MediatorLiveData<String> = MediatorLiveData()

    init {
        premiaField.addSource(premia) {
            premiaField.postValue(it)
        }
        premiaField.addSource(premia2) {
            premiaField.postValue(it)
        }
    }

    fun setMonth(yearMonth: YearMonth, calendar: List<DayStatus>) {
        _currentMonth.value = WorkMonth(yearMonth, calendar, statusList.value ?: listOf(), yearMonthDataList.value ?: listOf())
    }

    fun setMonth(workMonth: WorkMonth) {
        _currentMonth.value = workMonth
    }

    fun onUncheckedCountFuture() {
        setMonth(currentMonth.value?.yearMonth ?: YearMonth.now(),
            calendar.filter { it.endDateTime.isBefore(LocalDateTime.now()) })
    }

    fun onCheckedCountFuture() {
        setMonth(currentMonth.value?.yearMonth ?: YearMonth.now(), calendar)
    }

    fun logWorkMonth(workMonth: WorkMonth) {
        logd(WorkMonthSalaryCounter(workMonth).logList())
    }

    fun setPremia(value: Double) {
        savePremia(value)

    }


    private fun savePremia(value: Double) {
        viewModelScope.launch {
            currentMonth.value?.let {
                yearMonthRepository.insert(YearMonthData(it.yearMonth.toInt(), value))
            }
        }
    }

    fun initializeWorkMonth(): WorkMonth {
        return WorkMonth.of(yearMonth,
            calendar.filter { it.endDateTime.isBefore(LocalDateTime.now()) },
            statusList.value ?: listOf(),
            yearMonthDataList.value ?: listOf(),
            fiveDayWeek = fiveDayWeek
        )
    }

}

class NormaViewModelFactory(
    private val calendar: List<DayStatus>,
    private val statusList: LiveData<List<MachinistStatus>>,
    private val yearMonthLiveData: LiveData<List<YearMonthData>>,
    private val yearMonthRepository: YearMonthRepository,
    private val yearMonth: YearMonth,
    private val fiveDayWeek: Boolean
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NormaViewModel::class.java)) {
//            logd("""
//                NVMFactory
//                Calendar === ${calendar.size}
//                Statuses === $statusList
//            """.trimIndent())
            @Suppress("UNCHECKED_CAST")
            return NormaViewModel(calendar, statusList, yearMonthLiveData, yearMonthRepository, yearMonth, fiveDayWeek) as T
        }
        throw IllegalStateException("Unknown ViewModel class")
    }
}