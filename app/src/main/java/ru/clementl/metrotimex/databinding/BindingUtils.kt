package ru.clementl.metrotimex.databinding

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import ru.clementl.metrotimex.model.data.*
import ru.clementl.metrotimex.model.states.AdvancedState
import ru.clementl.metrotimex.model.states.SimpleState
import ru.clementl.metrotimex.utils.*
import ru.clementl.metrotimex.viewmodel.ShiftDetailViewModel
import java.time.Duration

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
        visibility = if (it.isReserve == true) View.VISIBLE else View.GONE
    }
}

@BindingAdapter("showAtz")
fun TextView.showAtz(day: DayStatus?) {
    day?.shift?.let {
        visibility = if (it.hasAtz == true) View.VISIBLE else View.GONE
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

@BindingAdapter("advancedStateDesc")
fun TextView.advancedStateDesc(advancedState: LiveData<AdvancedState>) {
    advancedState.value?.let {
        logd("in let")
        text = it.description
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

@BindingAdapter("durationString")
fun TextView.showDurationString(day: DayStatus?) {
    day?.shift?.let {
        val dur = day.timeSpan.duration ?: 0L
        text = "Длительность: ${(Duration.ofMillis(dur)).inFloatHours(true)}"
    }
}

@BindingAdapter("notesLabel")
fun ImageView.notesLabelVisibility(day: DayStatus?) {
    day?.notes?.let {
        if (it.isNotEmpty()) visibility = View.VISIBLE else visibility = View.GONE
    }
    if (day?.notes == null) visibility = View.GONE
}


