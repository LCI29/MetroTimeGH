package ru.clementl.metrotimex.screens.norma

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.clementl.metrotimex.model.data.MachinistStatus
import ru.clementl.metrotimex.repositories.MachinistStatusRepository
import ru.clementl.metrotimex.repositories.YearMonthRepository
import java.lang.IllegalStateException

class StatusViewModel(
    val machinistStatusRepository: MachinistStatusRepository,
    val yearMonthRepository: YearMonthRepository
) : ViewModel() {

    val liveStatusList: LiveData<List<MachinistStatus>> = machinistStatusRepository.getAllAsLiveData()
    val liveYearMonthData: LiveData<List<YearMonthData>> = yearMonthRepository.getAllAsLiveData()
}

class StatusViewModelFactory(
    private val machinistStatusRepository: MachinistStatusRepository,
    private val yearMonthRepository: YearMonthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatusViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatusViewModel(machinistStatusRepository, yearMonthRepository) as T
        }
        throw IllegalStateException("Unknown ViewModel class")
    }
}