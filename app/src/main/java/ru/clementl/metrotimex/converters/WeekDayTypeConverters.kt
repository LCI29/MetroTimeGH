package ru.clementl.metrotimex.converters

import ru.clementl.metrotimex.model.data.WeekDayType
import java.util.*


fun WeekDayType.toStringCode() = code

fun String.toWeekDayType(): WeekDayType {
    return when (toUpperCase(Locale.getDefault())) {
        "Р" -> WeekDayType.WORKDAY
        "В" -> WeekDayType.WEEKEND
        else -> WeekDayType.UNKNOWN
    }
}
