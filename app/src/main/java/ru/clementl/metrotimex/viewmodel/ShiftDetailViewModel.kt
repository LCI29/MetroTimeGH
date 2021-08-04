package ru.clementl.metrotimex.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.repositories.CalendarRepository
import ru.clementl.metrotimex.utils.logd

class ShiftDetailViewModel(
    val dayId: Long = 0L,
    val dataSource: CalendarRepository
) : ViewModel() {

    private val day = MediatorLiveData<DayStatus>()

    fun getDay() = day

    init {
        val d = dataSource.getLiveDayByDate(dayId)
        day.addSource(d, day::setValue)
    }

    /**
     * Variable that tells the fragment whether it should navigate to [CalendarFragment].
     *
     * This is `private` because we don't want to expose the ability to set [MutableLiveData] to
     * the [Fragment]
     */
    private val _navigateToCalendar = MutableLiveData<Boolean?>()

    /**
     * When true immediately navigate back to the [CalendarFragment]
     */
    val navigateToCalendar: LiveData<Boolean?>
        get() {
            logd("navigateToCalendar = ${_navigateToCalendar.value}")
            return _navigateToCalendar
        }

    /**
     * Call this immediately after navigating to [CalendarFragment]
     */
    fun doneNavigating() { _navigateToCalendar.value = null}

    fun onClose() { _navigateToCalendar.value = true}

    fun deleteDay(dayId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            dataSource.delete(dayId)
        }
    }


}

class ShiftDetailViewModelFactory(
    val dayId: Long,
    val dataSource: CalendarRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShiftDetailViewModel::class.java)) {
            return ShiftDetailViewModel(dayId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


