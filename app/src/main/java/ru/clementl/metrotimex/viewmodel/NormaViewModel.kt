package ru.clementl.metrotimex.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.WorkDayType
import ru.clementl.metrotimex.model.norma.WorkMonth
import java.lang.IllegalStateException
import java.time.LocalDateTime
import java.time.YearMonth

class NormaViewModel(val calendar: List<DayStatus>, yearMonth: YearMonth) : ViewModel() {

    private val uiScope = CoroutineScope(Job() + Dispatchers.Main)

    private val _currentMonth = MutableLiveData<WorkMonth>(WorkMonth.of(yearMonth,
        calendar.filter { it.endDateTime.isBefore(LocalDateTime.now()) }))
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

    fun setMonth(yearMonth: YearMonth, calendar: List<DayStatus>) {
        _currentMonth.value = WorkMonth(yearMonth, calendar)
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

}

class NormaViewModelFactory(
    private val calendar: List<DayStatus>,
    private val yearMonth: YearMonth
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NormaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NormaViewModel(calendar, yearMonth) as T
        }
        throw IllegalStateException("Unknown ViewModel class")
    }
}