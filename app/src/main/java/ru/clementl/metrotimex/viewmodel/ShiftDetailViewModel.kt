package ru.clementl.metrotimex.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.MachinistStatus
import ru.clementl.metrotimex.model.data.finalSalary
import ru.clementl.metrotimex.model.data.getMachinistStatus
import ru.clementl.metrotimex.repositories.CalendarRepository
import ru.clementl.metrotimex.repositories.MachinistStatusRepository
import ru.clementl.metrotimex.utils.logd

class ShiftDetailViewModel(
    val dayId: Long = 0L,
    val calendarRepository: CalendarRepository,
    val machinistStatusRepository: MachinistStatusRepository
) : ViewModel() {

    val mDay = MediatorLiveData<DayStatus>()

    fun getDay() = mDay

    val mStatus = MediatorLiveData<MachinistStatus>()

    fun getStatus() = mStatus







    init {
        val d = calendarRepository.getLiveDayByDate(dayId)
        mDay.addSource(d, mDay::setValue)
        val liveStatus = machinistStatusRepository.getAllAsLiveData().switchMap { list ->
            MutableLiveData<MachinistStatus>(mDay.value?.dateLong.getMachinistStatus(list))
        }
        mStatus.addSource(liveStatus, mStatus::setValue)

    }



    val finalSalary: LiveData<Double>
        get() = MutableLiveData(mStatus.value?.let { mDay.value?.finalSalary(it) ?: 0.0 } ?: 0.0)

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
            calendarRepository.delete(dayId)
        }
    }


}

class ShiftDetailViewModelFactory(
    val dayId: Long,
    val calendarRepository: CalendarRepository,
    val machinistStatusRepository: MachinistStatusRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShiftDetailViewModel::class.java)) {
            return ShiftDetailViewModel(dayId, calendarRepository, machinistStatusRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



