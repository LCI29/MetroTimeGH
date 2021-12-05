package ru.clementl.metrotimex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.clementl.metrotimex.utils.salaryStyle

class YearBonusViewModel : ViewModel() {

    private val _result = MutableLiveData<Double>(0.0)
    val result: LiveData<Double>
        get() = _result

    private val _yearQ = MutableLiveData(7.0)
    val yearQ: LiveData<Double>
        get() = _yearQ

    private val _sgp = MutableLiveData(0.0)
    val sgp: LiveData<Double>
        get() = _sgp

    fun calculateBonus(stageQ: Double, inUnion: Boolean) {
        val futureSgp = (sgp.value ?: 0.0) / 11 * 12
        _result.value = futureSgp * stageQ * (yearQ.value?.div(100) ?: 0.0) * (if (inUnion) 0.86 else  0.87)
    }

    fun yearQInc() {
        _yearQ.value = _yearQ.value?.plus(0.1)
    }

    fun yearQDec() {
        _yearQ.value = _yearQ.value?.minus(0.1)
    }

    fun setSgp(value: Double) {
        _sgp.value = value
    }


}