package ru.clementl.metrotimex.viewmodel

import androidx.lifecycle.ViewModel
import ru.clementl.metrotimex.model.data.DayStatus

class SharedViewModel : ViewModel() {

    var currentDay: DayStatus? = null


}