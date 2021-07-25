package ru.clementl.metrotimex.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Year
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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

fun main() {
    val a = LocalDate.now().asSimpleDate()
    println(a)
}

