package ru.clementl.metrotimex.model.data

import ru.clementl.metrotimex.model.states.TimePoint

/**
 * Basic representation of any time interval
 */
interface TimeSpan {
    val startPoint: TimePoint?
    val endPoint: TimePoint?
    val duration: Long?
        get() {
            val a = startPoint?.milli ?: return null
            val b = endPoint?.milli ?: return null
            return b - a
        }

    operator fun contains(timePoint: TimePoint): Boolean {
        val a = startPoint?.milli ?: return false
        val b = endPoint?.milli ?: return false
        return timePoint.milli in a..b
    }

    fun fromStartTill(millis: Long): Long? {
        val a = startPoint?.milli ?: return null
        return millis - a
    }

    fun tillEndFrom(millis: Long): Long? {
        val b = endPoint?.milli ?: return null
        return b - millis
    }
}

/**
 * Comparing time in millis with TimeSpan. If the time is before start of TimeSpan, then it less;
 * if the time is after end of TimeSpan, then it greater; and if time is between start and end of
 * TimeSpan, then it equals
 */
operator fun Long.compareTo(timeSpan: TimeSpan): Int {
    timeSpan.startPoint?.let { a ->
        timeSpan.endPoint?.let { b ->
            return when {
                this < a.milli -> -1
                this > b.milli -> 1
                else -> 0
            }
        }
        return if (this < a.milli) -1 else 0
    }
    timeSpan.endPoint?.let { b ->
        return if (this > b.milli) 1 else 0
    }
    return 1
}

