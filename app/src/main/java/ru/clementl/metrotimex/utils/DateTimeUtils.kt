package ru.clementl.metrotimex.utils

import ru.clementl.metrotimex.*
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
    return if (endTime.isBefore(ODD_EVEN_DIFFER_AFTER) && endTime.isAfter(LATEST_NIGHT_END)) {
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
        "${if (days > 0) "${days}д " else ""}${noDaysTime.toTime().ofPattern("H" +
                ":mm:ss")}"
    } else {
        "${hours}:${noHoursTime.toTime().ofPattern("mm:ss")}"
    }
}

fun Duration.inFloatHours(withH: Boolean = true): String {
    return String.format("%.1f${if (withH) "ч" else ""}", toMillis().toDouble() / HOUR_MILLI)
}

fun Long.inFloatHours(withH: Boolean = true): String {
    return Duration.ofMillis(this).inFloatHours(withH)
}

fun Double.inFloatHours(withH: Boolean = true, decimals: Int = 1): String {
    return String.format("%.${decimals}f${if (withH) "ч" else ""}", this)
}


/**
 * Returns YearMonth as "АВГУСТ 2021"
 */
fun YearMonth.asShortString(): String {
    val month = when (month) {
        Month.JANUARY -> "Янв"
        Month.FEBRUARY -> "Фев"
        Month.MARCH -> "Март"
        Month.APRIL -> "Апр"
        Month.MAY -> "Май"
        Month.JUNE -> "Июнь"
        Month.JULY -> "Июль"
        Month.AUGUST -> "Авг"
        Month.SEPTEMBER -> "Сен"
        Month.OCTOBER -> "Окт"
        Month.NOVEMBER -> "Ноя"
        Month.DECEMBER -> "Дек"
        else -> ""
    }
    return "$month $year"
}

// Returns Int like 202107 on July 2021
fun YearMonth.toInt(): Int =
    if (monthValue < 10) "${year}0$monthValue".toInt() else "${year}$monthValue".toInt()

// Returns YearMonth from string like "202107" - July 2021. Throws DateTimeException
// if month will be not in 1..12
fun Int.toYearMonth(): YearMonth = YearMonth.of(this / 100, this % 100)


fun main() {
    println((202113).toYearMonth())
}

