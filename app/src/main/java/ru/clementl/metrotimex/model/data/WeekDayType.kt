package ru.clementl.metrotimex.model.data

import java.time.DayOfWeek
import java.time.LocalDate

/**
 * Determines either this day workday or weekend
 *
 */
enum class WeekDayType(val code: String) {
    WORKDAY("Р"),
    WEEKEND("В"),
    UNKNOWN("Н")
}

fun LocalDate.weekDayType(): WeekDayType {
    return if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)
        WeekDayType.WEEKEND else WeekDayType.WORKDAY
}