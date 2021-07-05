package ru.clementl.metrotimex.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

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

fun main() {
    val a = LocalDate.now().asSimpleDate()
    println(a)
}