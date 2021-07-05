package ru.clementl.metrotimex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.clementl.metrotimex.utils.logd
import java.time.LocalDate
import java.time.LocalTime

class ShiftCreateViewModel : ViewModel() {

    private var initialDate = LocalDate.now()
    private var initialStartTime = LocalTime.of(8, 0)
    private var initialEndTime = LocalTime.of(16, 0)

    private var _startDate = MutableLiveData<LocalDate>(initialDate)
    val startDate: LiveData<LocalDate> = _startDate

    private var _startTime = MutableLiveData<LocalTime>(initialStartTime)
    val startTime: LiveData<LocalTime> = _startTime

    private var _endTime = MutableLiveData<LocalTime>(initialEndTime)
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
    }

    override fun onCleared() {
        super.onCleared()
        logd("ShiftCreateViewModel cleared")
    }
}