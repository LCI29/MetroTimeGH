package ru.clementl.metrotimex.databinding

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.WorkDayType
import ru.clementl.metrotimex.model.states.SimpleState
import ru.clementl.metrotimex.utils.asSimpleDate
import ru.clementl.metrotimex.utils.asSimpleTime
import ru.clementl.metrotimex.utils.logd
import ru.clementl.metrotimex.utils.ofPattern
import ru.clementl.metrotimex.viewmodel.TonightViewModel

@BindingAdapter("dayOfMonth")
fun TextView.setDate(day: DayStatus?) {
    day?.let {
        text = day.date.dayOfMonth.toString()
    }
}

@BindingAdapter("dayOfWeek")
fun TextView.setDayOfWeek(day: DayStatus?) {
    day?.let {
        text = day.date.ofPattern("EE")
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("dayNameWithOddEven")
fun TextView.setDayNameWithOddEven(day: DayStatus?) {
    day?.let {
        if (day.workDayType == WorkDayType.SHIFT) {
            val shift = it.shift
            val oddEvenPostfix = if (shift?.oddEvenString?.isEmpty() != false) "" else "(${shift.oddEvenString})"
            text = "${shift?.name} $oddEvenPostfix"
        } else {
            text = day.workDayType?.desc
        }
    }
}

@BindingAdapter("dayName")
fun TextView.setDayName(day: DayStatus?) {
    day?.let {
        if (day.workDayType == WorkDayType.SHIFT) {
            text = "${it.shift?.name}"
        } else {
            text = day.workDayType?.desc
        }
    }
}

@BindingAdapter("shiftNameOrEmpty")
fun TextView.setShiftNameOrEmpty(day: DayStatus?) {
    day?.shift?.let {
        text = "${it.name}"
    } ?: run {text = ""}
}

@BindingAdapter("descString")
fun TextView.setDescString(day: DayStatus?) {
    day?.let {
        day.shift?.let {
            text = it.getDescriptionString()
        }
    }
}

@BindingAdapter("fullDate")
fun TextView.setFullDate(day: DayStatus?) {
    day?.let {
        text = it.date.ofPattern("d MMMM yyyy, EE")
    }
}

@BindingAdapter("simpleStartDate")
fun TextView.setSimpleDate(day: DayStatus?) {
    day?.let {
        text = day.date.asSimpleDate()
    }
}

@BindingAdapter("simpleStartTime")
fun TextView.setSimpleStartTime(day: DayStatus?) {
    day?.shift?.let {
        text = it.startTime?.asSimpleTime()
    }
}

@BindingAdapter("simpleEndTime")
fun TextView.setSimpleEndTime(day: DayStatus?) {
    day?.shift?.let {
        text = it.endTime?.asSimpleTime()
    }
}

@BindingAdapter("startLoc")
fun TextView.setStartLoc(day: DayStatus?) {
    day?.shift?.startLoc?.let {
        text = it
    }
}

@BindingAdapter("endLoc")
fun TextView.setEndLoc(day: DayStatus?) {
    day?.shift?.endLoc?.let {
        text = it
    }
}

@BindingAdapter("showReserve")
fun TextView.showReserve(day: DayStatus?) {
    day?.shift?.let {
        if (it.isReserve == true) visibility = View.VISIBLE
    }
}

@BindingAdapter("showAtz")
fun TextView.showAtz(day: DayStatus?) {
    day?.shift?.let {
        if (it.hasAtz == true) visibility = View.VISIBLE
    }
}

@BindingAdapter("simpleStateDesc")
fun TextView.simpleStateDesc(simpleState: LiveData<SimpleState>) {
    logd("onStart simpleStateDesc BindingAdapter")
    simpleState.value?.let {
        logd("in let")
        text = it.desc
        logd(text.toString())
        return
    }
    text = "null"
}

@BindingAdapter("showLiveDataLong")
fun TextView.showLiveDataString(source: LiveData<String>?) {
    source?.value?.let {
        text = it
    }
}