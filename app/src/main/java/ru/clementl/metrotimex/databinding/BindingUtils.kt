package ru.clementl.metrotimex.databinding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.WorkDayType
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