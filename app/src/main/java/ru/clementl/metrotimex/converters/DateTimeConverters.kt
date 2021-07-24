package ru.clementl.metrotimex.converters

import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*


fun LocalDate.toLong(): Long {
    return atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun Long.toDate(): LocalDate {
    return LocalDateTime.ofInstant(Date(this).toInstant(), ZoneId.systemDefault()).toLocalDate()
}

fun LocalTime.toInt() = toSecondOfDay()

fun Int.toTime() = LocalTime.ofSecondOfDay(toLong())



