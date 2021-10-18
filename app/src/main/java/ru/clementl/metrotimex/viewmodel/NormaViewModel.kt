package ru.clementl.metrotimex.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.MachinistStatus
import ru.clementl.metrotimex.model.data.WorkDayType
import ru.clementl.metrotimex.model.norma.WorkMonth
import ru.clementl.metrotimex.model.salary.WorkMonthSalaryCounter
import ru.clementl.metrotimex.utils.inFloatHours
import ru.clementl.metrotimex.utils.logd
import ru.clementl.metrotimex.utils.salaryStyle
import java.lang.Exception
import java.lang.IllegalStateException
import java.time.Duration
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.concurrent.TimeUnit
import kotlin.time.toDuration

class NormaViewModel(
    val calendar: List<DayStatus>,
    val statusList: LiveData<List<MachinistStatus>>,
    yearMonth: YearMonth
) : ViewModel() {



//    init {
//        logd("NVM: StatusList = ${statusList.value}")
//    }

    private val uiScope = CoroutineScope(Job() + Dispatchers.Main)

    private val _currentMonth = MutableLiveData<WorkMonth>(WorkMonth.of(yearMonth,
        calendar.filter { it.endDateTime.isBefore(LocalDateTime.now()) }, statusList.value ?: listOf()))
    val currentMonth: LiveData<WorkMonth>
        get() = _currentMonth

    val weekendString: LiveData<String> = Transformations.map(currentMonth) {
        it.weekendString
    }

    val normaString: LiveData<String> = Transformations.map(currentMonth) {
        it.normaString
    }

    val workDayString: LiveData<String> = Transformations.map(currentMonth) {
        it.workdayString
    }

    val sickListDaysString: LiveData<String> = Transformations.map(currentMonth) {
        it.countOf(WorkDayType.SICK_LIST).toString()
    }

    val vacationDaysString: LiveData<String> = Transformations.map(currentMonth) {
        it.countOf(WorkDayType.VACATION_DAY).toString()
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

    val totalSalaryString: LiveData<String> = Transformations.map(currentMonth) {
        it.totalSalary.salaryStyle()
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

    fun setMonth(yearMonth: YearMonth, calendar: List<DayStatus>) {
        _currentMonth.value = WorkMonth(yearMonth, calendar, statusList.value ?: listOf())
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

}

class NormaViewModelFactory(
    private val calendar: List<DayStatus>,
    private val statusList: LiveData<List<MachinistStatus>>,
    private val yearMonth: YearMonth
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NormaViewModel::class.java)) {
//            logd("""
//                NVMFactory
//                Calendar === ${calendar.size}
//                Statuses === $statusList
//            """.trimIndent())
            @Suppress("UNCHECKED_CAST")
            return NormaViewModel(calendar, statusList, yearMonth) as T
        }
        throw IllegalStateException("Unknown ViewModel class")
    }
}