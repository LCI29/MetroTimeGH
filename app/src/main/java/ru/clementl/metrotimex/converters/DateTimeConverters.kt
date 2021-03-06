package ru.clementl.metrotimex.converters

import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.time.*
import java.util.*


fun LocalDate.toLong(): Long {
    return atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun Long.toDate(): LocalDate {
    return toDateTime().toLocalDate()
}

fun LocalTime.toInt() = toSecondOfDay() // for DB

fun Int.toTime() = LocalTime.ofSecondOfDay(toLong()) // for DB

fun Long.toTime() = (this / 1000).toInt().toTime()

fun LocalDateTime.toLong(): Long {
    return atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun Long.toDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
}

fun LocalTime.toLong(): Long {
    return toSecondOfDay().toLong() * 1000
}

fun String.fromAmericanToDate(): LocalDate {
    val (month, day, year) = split("/")
    return LocalDate.of(year.toInt(), month.toInt(), day.toInt())
}



