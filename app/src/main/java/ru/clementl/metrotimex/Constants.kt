package ru.clementl.metrotimex

import ru.clementl.metrotimex.converters.toInt
import ru.clementl.metrotimex.converters.toLong
import java.time.LocalDateTime
import java.time.LocalTime

// Basic rate per hour in Metropolitan
const val RATE_PER_HOUR = 375.41

// Types for ShiftCreateFragment
const val SHIFT_CREATING = 1000
const val SHIFT_EDITING = 2000
const val NO_DAY_ID = -1L

// Basic start and end of a shift
const val DEFAULT_START_HOUR = 8
const val DEFAULT_END_HOUR = 16
const val DEFAULT_MINUTE = 0

// TimePoint types: SP = Start Point, EP = End Point.
const val UNKNOWN_SP = -1
const val UNKNOWN_EP = -2
const val SHIFT_SP = 1
const val SHIFT_EP = 2
const val WEEKEND_SP = 11
const val WEEKEND_EP = 12
const val SICK_DAY_SP = 21
const val SICK_DAY_EP = 22
const val VACATION_DAY_SP = 31
const val VACATION_DAY_EP = 32
const val MEDIC_DAY_SP = 41
const val MEDIC_DAY_EP = 42

// Время смены суток для метро, определяется наиболее поздним возможным окончанием неночной смены
val DAY_CHANGE_TIME: LocalTime = LocalTime.of(3, 0)
val DAY_START_TIME: LocalTime = DAY_CHANGE_TIME
val DAY_END_TIME: LocalTime = DAY_START_TIME.minusNanos(1)

// Description constants
const val SHIFT_STRING = "Смена"
const val WEEKEND_STRING = "Выходной"
const val SICK_DAY_STRING = "Больничный"
const val SICK_DAY_STRING_SHORT = "Б/Л"
const val VACATION_DAY_STRING = "Отпуск"
const val MEDIC_DAY = "Медкомиссия"
const val NEXT_SHIFT_STRING = "Следующая смена"
const val SHIFT_FINISHED_STRING = "Смена завершена"
const val GAP_STRING = "Разрыв"
const val NIGHT_GAP_STRING = "Ночной разрыв"

const val NO_DATA_STRING = "Нет данных"

// set of all the TimePoints types, which represent START of the interval
val START_POINTS = setOf(SHIFT_SP, WEEKEND_SP, SICK_DAY_SP, VACATION_DAY_SP, MEDIC_DAY_SP, UNKNOWN_SP)

// set of all the TimePoints types, which represent END of the interval
val END_POINTS = setOf(SHIFT_EP, WEEKEND_EP, SICK_DAY_EP, VACATION_DAY_EP, MEDIC_DAY_EP, UNKNOWN_EP)

val DAYS_FOR_TONIGHT_BEFORE = 1 // Days before today to load from db for TonightFragment
val DAYS_FOR_TONIGHT_AFTER = 5 // Days after today to load from db for TonightFragment, today inclusive

// Maximum duration of the Night Gap, millis
val NIGHT_GAP_MAX_DURATION = LocalTime.of(7, 0).toLong()

// Duration of AfterShiftAdvancedState priority
val AFTER_SHIFT_STATE_PRIORITY_DURATION = LocalTime.of(0, 30).toLong()