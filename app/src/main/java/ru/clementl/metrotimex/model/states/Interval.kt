package ru.clementl.metrotimex.model.states

import ru.clementl.metrotimex.model.data.DayStatus

data class Interval(val startPoint: TimePoint?, val endPoint: TimePoint?) {
    val simpleState: SimpleState by lazy { simpleState() }
}

fun Long.getInterval(calendar: List<DayStatus>): Interval {
    val sortedCalendar = calendar.sortedBy { it.date }
    val lastDayBefore = sortedCalendar.findLast { it.startPoint.dateTimeLong <= this }
    val pointBefore = getPointBeforeFrom(lastDayBefore)
    val firstDayAfter = sortedCalendar.find { it.endPoint.dateTimeLong > this }
    val pointAfter = getPointAfterFrom(firstDayAfter)
    return Interval(pointBefore, pointAfter)
}

fun Long.getPointBeforeFrom(day: DayStatus?): TimePoint? {
    day?.let {
        return when {
            (it.endPoint.dateTimeLong <= this) -> it.endPoint
            (it.startPoint.dateTimeLong <= this) -> it.startPoint
            else -> null
        }
    }
    return null
}

fun Long.getPointAfterFrom(day: DayStatus?): TimePoint? {
    day?.let {
        return when {
            (it.startPoint.dateTimeLong > this) -> it.startPoint
            (it.endPoint.dateTimeLong > this) -> it.endPoint
            else -> null
        }
    }
    return null
}