package ru.clementl.metrotimex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.clementl.metrotimex.utils.salaryStyle

class YearBonusViewModel : ViewModel() {

    private val _result = MutableLiveData<Double>(0.0)
    val result: LiveData<String> = Transformations.map(_result) {
        it.salaryStyle()
    }

    fun calculateBonus(yearQ: Double, stageQ: Double, base: Double, inUnion: Boolean) {
        _result.value = base * stageQ * yearQ * (if (inUnion) 0.86 else  0.87)
    }


}