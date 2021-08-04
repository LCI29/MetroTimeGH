package ru.clementl.metrotimex.model.states

import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.TimeSpan

/**
 * Represents an Interval of time with nullable TimePoints. Interval has a state, determined by
 * types of its start and end TimePoints
 */
data class Interval(override val startPoint: TimePoint?, override val endPoint: TimePoint?) :
    TimeSpan {
    val simpleState: SimpleState by lazy { simpleState() }
    override val duration: Long? by lazy {
        endPoint?.let {
            startPoint?.let {
                endPoint.dateTimeLong - startPoint.dateTimeLong
            }
        }
    }
}

/**
 * Returns [Interval], that this moment belongs to. Interval may have nullable TimePoints.
 * If [calendar] is empty, function returns Interval(null, null)
 */
fun Long.getInterval(calendar: List<DayStatus>): Interval {
    val sortedCalendar = calendar.sortedBy { it.date }
    val lastDayBefore = sortedCalendar.findLast { it.startPoint.dateTimeLong <= this }
    val pointBefore = getPointBeforeFrom(lastDayBefore)
    val firstDayAfter = sortedCalendar.find { it.endPoint.dateTimeLong > this }
    val pointAfter = getPointAfterFrom(firstDayAfter)
    return Interval(pointBefore, pointAfter)
}

/**
 * Returns TimePoint that is same or before [this] from [day]. If there is not returns null
 */
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

/**
 * Returns TimePoint that is after [this] from [day]. If there is not returns null
 */
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