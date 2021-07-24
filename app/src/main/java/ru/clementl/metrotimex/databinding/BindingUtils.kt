package ru.clementl.metrotimex.databinding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.WorkDayType
import ru.clementl.metrotimex.utils.asSimpleDate
import ru.clementl.metrotimex.utils.asSimpleTime
import ru.clementl.metrotimex.utils.ofPattern

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

@BindingAdapter("dayName")
fun TextView.setDayName(day: DayStatus?) {
    day?.let {
        if (day.workDayType == WorkDayType.SHIFT) {
            text = if (day.shift?.name?.isEmpty() != false) "Смена" else day.shift.name
        } else {
            text = day.workDayType?.desc
        }
    }
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

