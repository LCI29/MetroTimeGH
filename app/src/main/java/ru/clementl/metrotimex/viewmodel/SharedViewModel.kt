package ru.clementl.metrotimex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.Shift
import ru.clementl.metrotimex.model.data.Weekend
import ru.clementl.metrotimex.utils.logd
import java.time.LocalDate
import java.time.LocalTime

class SharedViewModel : ViewModel() {

    val shiftList: List<DayStatus> = mutableListOf(
        Shift(
            LocalDate.of(2021, 5, 21),
            LocalTime.of(8, 31),
            LocalTime.of(16, 25)
        ),

        Shift(
            LocalDate.of(2021, 5, 22),
            LocalTime.of(12, 15),
            LocalTime.of(20, 22), shiftName = "88.3"
        ),

        Weekend(LocalDate.of(2021, 5, 23))
    )

    override fun onCleared() {
        super.onCleared()
        logd("ShiftCreateViewModel cleared")
    }





}