package ru.clementl.metrotimex.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun LocalDateTime.asSimpleTime(): String {
    return format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
}

fun LocalTime.asSimpleTime(): String {
    return format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
}

fun LocalDateTime.asSimpleDate(): String {
    return format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
}

fun LocalDate.asSimpleDate(): String {
    return format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
}

private fun LocalDate.isDateEven() = dayOfMonth % 2 == 0

fun LocalDate.oddEven(endTime: LocalTime): Int {
    val oddEvenDifferAfter = LocalTime.of(22,30)
    val latestNightEnd = LocalTime.of(4,0)
    return if (endTime.isBefore(oddEvenDifferAfter) && endTime.isAfter(latestNightEnd)) {
        0
    } else {
        if (isDateEven()) 2 else 1
    }
}

fun LocalDate.ofPattern(pattern: String): String {
    return format(DateTimeFormatter.ofPattern(pattern))
}

fun main() {
    val a = LocalDate.now().asSimpleDate()
    println(a)
}