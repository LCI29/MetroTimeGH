package ru.clementl.metrotimex.model.data

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

const val TYPE_UNKNOWN = -1
const val TYPE_SHIFT = 0
const val TYPE_WEEKEND = 1
const val TYPE_SICK_LIST = 2
const val TYPE_VACATION_DAY = 3

sealed class DayStatus (val date: LocalDate) {abstract val type: Int}

class Shift(
    _startDate: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val shiftName: String = "Смена",
    val startPlace: String = "",
    val endPlace: String = ""
) : DayStatus(_startDate) {
    val endDate: LocalDate
        get() = if (endTime.isAfter(startTime)) date else date.plusDays(1)
    override val type = TYPE_SHIFT
}

class Weekend(date: LocalDate) : DayStatus(date) { override val type = TYPE_WEEKEND }

class SickListDay(date: LocalDate) : DayStatus(date) { override val type = TYPE_SICK_LIST }

class VacationDay(date: LocalDate) : DayStatus(date) { override val type = TYPE_VACATION_DAY }

class UnknownDay(date: LocalDate) : DayStatus(date) { override val type  = TYPE_UNKNOWN }