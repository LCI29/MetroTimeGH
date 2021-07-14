package ru.clementl.metrotimex.viewmodel

import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.repositories.DayStatusRepository
import java.lang.IllegalStateException

class CalendarViewModel(private val repository: DayStatusRepository) : ViewModel() {

    val allDays: LiveData<List<DayStatus>> = repository.allDays.asLiveData()

    fun insert(dayStatus: DayStatus) = viewModelScope.launch {
        repository.insert(dayStatus)
    }
}

class CalendarViewModelFactory(private val repository: DayStatusRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalendarViewModel(repository) as T
        }
        throw IllegalStateException("Unknown ViewModel class")
    }
}