package ru.clementl.metrotimex.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.repositories.CalendarRepository
import java.lang.IllegalStateException

class CalendarViewModel(private val repository: CalendarRepository) : ViewModel() {

    val allDays: LiveData<List<DayStatus>> = repository.allDays.asLiveData()

    fun insert(dayStatus: DayStatus) = viewModelScope.launch {
        repository.insert(dayStatus)
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