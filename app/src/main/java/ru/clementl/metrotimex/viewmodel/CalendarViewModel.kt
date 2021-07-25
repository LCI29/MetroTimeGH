package ru.clementl.metrotimex.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.repositories.CalendarRepository
import ru.clementl.metrotimex.utils.logd
import java.lang.IllegalStateException

class CalendarViewModel(private val repository: CalendarRepository) : ViewModel() {

    val allDays: LiveData<List<DayStatus>> = repository.allDays.asLiveData()

    fun insert(dayStatus: DayStatus) = viewModelScope.launch {
        repository.insert(dayStatus)
    }

    fun delete(dayId: Long) = viewModelScope.launch{
        repository.delete(dayId)
    }

    private val _navigateToShiftDetail = MutableLiveData<Long?>()
    val navigateToShiftDetail: LiveData<Long?>
        get() = _navigateToShiftDetail

    fun onShiftClicked(id: Long) {
        _navigateToShiftDetail.value = id
    }

    fun onShiftDetailNavigated() {
        _navigateToShiftDetail.value = null
    }

    override fun onCleared() {
        super.onCleared()
        logd("CalendarViewModel onCleared")
    }


}

class CalendarViewModelFactory(private val repository: CalendarRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalendarViewModel(repository) as T
        }
        throw IllegalStateException("Unknown ViewModel class")
    }
}