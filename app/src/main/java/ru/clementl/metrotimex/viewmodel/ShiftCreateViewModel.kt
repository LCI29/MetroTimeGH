package ru.clementl.metrotimex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.TypeConverters
import ru.clementl.metrotimex.converters.ShiftConverter
import ru.clementl.metrotimex.repositories.DayStatusRepository
import ru.clementl.metrotimex.utils.logd
import java.lang.IllegalStateException
import java.time.LocalDate
import java.time.LocalTime

class ShiftCreateViewModel() : ViewModel() {

    private val initialDate = LocalDate.now()
    private var initialStartTime = LocalTime.of(8, 0)
    private var initialEndTime = LocalTime.of(16, 0)

    private var _startDate = MutableLiveData(initialDate)
    val startDate: LiveData<LocalDate> = _startDate

    private var _startTime = MutableLiveData(initialStartTime)
    val startTime: LiveData<LocalTime> = _startTime

    private var _endTime = MutableLiveData(initialEndTime)
    val endTime: LiveData<LocalTime> = _endTime


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
        _startDate.value = initialDate
        _startTime.value = initialStartTime
        _endTime.value = initialEndTime
    }

    override fun onCleared() {
        super.onCleared()
        logd("ShiftCreateViewModel cleared")
    }



}
