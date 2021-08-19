package ru.clementl.metrotimex.model.states

import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.TimeSpan
import ru.clementl.metrotimex.utils.logd
import kotlin.math.absoluteValue

/**
 * Represents an Interval of time with nullable TimePoints. Interval has a state, determined by
 * types of its start and end TimePoints
 */
data class Interval(val startPoint: TimePoint?, val endPoint: TimePoint?) :
    TimeSpan(startPoint?.milli, endPoint?.milli) {
    override val startMilli: Long?
        get() = startPoint?.milli
    override val endMilli: Long?
        get() = endPoint?.milli
    val simpleState: SimpleState by lazy { simpleState() }
    override val duration: Long? by lazy {
        endPoint?.let {
            startPoint?.let {
                endPoint.milli - startPoint.milli
            }
        }
    }
}

fun Long.getUnitedInterval(calendar: List<DayStatus>): Interval {
    val baseInterval = getInterval(calendar)
    var start = baseInterval.startPoint ?: return baseInterval
    var end = baseInterval.endPoint ?: return baseInterval

//    logd("Interval state = ${baseInterval.simpleState}, ${baseInterval.startPoint}, ${baseInterval.endPoint}")

    var previousInterval = (start.milli - 2).getInterval(calendar)

    while (previousInterval.simpleState == baseInterval.simpleState && previousInterval.startPoint != null) {
//        logd("PreviousInterval state = ${previousInterval.simpleState}, ${previousInterval.startPoint}, ${previousInterval.endPoint}")
        start = previousInterval.startPoint!!
        previousInterval = (start.milli - 2).getInterval(calendar)
    }
    var nextInterval = (end.milli + 2).getInterval(calendar)
    while (nextInterval.simpleState == baseInterval.simpleState) {
        end = nextInterval.endPoint!!
        nextInterval = (end.milli + 2).getInterval(calendar)
    }
    return Interval(start, end)
}

/**
 * Returns [Interval], that this moment belongs to. Interval may have nullable TimePoints.
 * If [calendar] is empty, function returns Interval(null, null)
 */
fun Long.getInterval(calendar: List<DayStatus>): Interval {
    logd("Long.getInterval()")
    val allPoints = calendar.map { it.startPoint to it.endPoint }.flatMap { it.toList() }.sortedBy { it.milli }
    val pointBefore = allPoints.findLast { it.milli <= this }
    val pointAfter = allPoints.find { it.milli > this }
    return Interval(pointBefore, pointAfter)
}

/**
 * Returns TimePoint that is same or before [this] from [day]. If there is not returns null
 */
fun Long.getPointBeforeFrom(day: DayStatus?): TimePoint? {
    day?.let {
        return when {
            (it.endPoint.milli <= this) -> it.endPoint
            (it.startPoint.milli <= this) -> it.startPoint
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
            (it.startPoint.milli > this) -> it.startPoint
            (it.endPoint.milli > this) -> it.endPoint
            else -> null
        }
    }
    return null
}

fun Long.closeTo(interval: Interval): Boolean {
    val startMilli = interval.startMilli ?: return true
    val endMilli = interval.endMilli ?: return true
    val b = (this - startMilli).absoluteValue < 30000 ||
            (this - endMilli).absoluteValue < 30000
    logd("Long.closeTo()")
    return b
}