package ru.clementl.metrotimex.utils

import ru.clementl.metrotimex.DAY_MILLI
import ru.clementl.metrotimex.HOUR_MILLI
import ru.clementl.metrotimex.MINUTE_MILLI
import ru.clementl.metrotimex.SECOND_MILLI
import ru.clementl.metrotimex.converters.toDateTime
import ru.clementl.metrotimex.converters.toTime
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.*

fun LocalDateTime.asSimpleTime(): String {
    return format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
}

fun LocalTime.asSimpleTime(): String {
    return format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
}

fun LocalDateTime.asSimpleDate(withYear: Boolean = true): String {
    return toLocalDate().asSimpleDate(withYear)
}

fun LocalDate.asSimpleDate(withYear: Boolean = true): String {
    return if (withYear) format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
    else ofPattern("d.MM")
}


fun LocalDate.fullDate(withDayOfWeek: Boolean = true): String {
    return ofPattern(if (withDayOfWeek) "d MMMM yyyy, EE" else "d MMMM yyyy")
}

private fun LocalDate.isDateEven() = dayOfMonth % 2 == 0

fun LocalDate.oddEven(endTime: LocalTime): Int {
    val oddEvenDifferAfter = LocalTime.of(22,30)
    val latestNightEnd = LocalTime.of(4,0)
    return if (endTime.isBefore(oddEvenDifferAfter) && endTime.isAfter(latestNightEnd)) {
        0
    } else {
        if (plusDays(1).isDateEven()) 2 else 1
    }
}

fun LocalDate.ofPattern(pattern: String): String {
    return format(DateTimeFormatter.ofPattern(pattern))
}

fun LocalTime.ofPattern(pattern: String): String {
    return format(DateTimeFormatter.ofPattern(pattern))
}

fun Long.ofPatternDays(correction: Int = 0): String {
    val fullDays = this / DAY_MILLI
    return "${fullDays + correction}д"
}

fun Long.ofPatternTime(showDays: Boolean = false): String {
    val days = this / DAY_MILLI
    val noDaysTime = this % DAY_MILLI
    val hours = this / HOUR_MILLI
    val noHoursTime = this % HOUR_MILLI
    return if (showDays) {
        "${if (days > 0) "${days}д " else ""}${noDaysTime.toTime().ofPattern("h:mm:ss")}"
    } else {
        "${hours}:${noHoursTime.toTime().ofPattern("mm:ss")}"
    }
}

fun Duration.inFloatHours(withH: Boolean = true): String {
    return String.format("%.1f${if (withH) "ч" else ""}", toMillis().toDouble() / HOUR_MILLI)
}

fun Double.inFloatHours(withH: Boolean = true, decimals: Int = 1): String {
    return String.format("%.${decimals}f${if (withH) "ч" else ""}", this)
}

fun YearMonth.asShortString(): String {
    return "${month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()).capitalize(Locale.ROOT)} ${year}"
}

fun main() {
    val a: Long = (2 * DAY_MILLI + 2* HOUR_MILLI + 2* MINUTE_MILLI + 2* SECOND_MILLI).toLong()
    println(a.ofPatternTime())
}

