package ru.clementl.metrotimex.viewmodel

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.clementl.metrotimex.repositories.CalendarRepository
import ru.clementl.metrotimex.utils.LOG_TAG
import ru.clementl.metrotimex.utils.logd
import java.lang.IllegalStateException
import java.time.*

class ShiftCreateViewModel(val repository: CalendarRepository) : ViewModel() {

    private val uiScope = CoroutineScope(Job() + Dispatchers.Main)

    private var initialStartTime = LocalTime.of(8, 0)
    private var initialEndTime = LocalTime.of(16, 0)

    private var _startDate = MutableLiveData<LocalDate>()
    val startDate: LiveData<LocalDate>
        get() = _startDate

    private var _startTime = MutableLiveData<LocalTime>()
    val startTime: LiveData<LocalTime>
        get() = _startTime

    private var _endTime = MutableLiveData<LocalTime>()
    val endTime: LiveData<LocalTime>
        get() = _endTime

    init {
        _startTime.value = initialStartTime
        _endTime.value = initialEndTime
        initializeStartDate()
    }

    fun initializeStartDate() {
        uiScope.launch {
            _startDate.value = getFirstFreeDateFromDb()
        }
    }

    private suspend fun getFirstFreeDateFromDb(): LocalDate {
        val offset = OffsetDateTime.now(ZoneId.systemDefault()).offset
        return withContext(Dispatchers.IO) {
            var epochmilli = repository.getLastDate().div(1000) ?: 0
            logd("${epochmilli}, ${LocalDateTime.ofEpochSecond(epochmilli, 0, offset)}")
            var date = LocalDateTime.ofEpochSecond(
                epochmilli, 0, offset).toLocalDate().plusDays(1)
            date
        }
    }


    fun setStartDate(date: LocalDate) {
        _startDate.value = date
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

    override fun onCleared() {
        super.onCleared()
        logd("ShiftCreateViewModel cleared")
    }



}

class ShiftCreateViewModelFactory(private val repository: CalendarRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShiftCreateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShiftCreateViewModel(repository) as T
        }
        throw IllegalStateException("Unknown ViewModel class")
    }
}