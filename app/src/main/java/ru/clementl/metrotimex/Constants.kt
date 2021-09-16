package ru.clementl.metrotimex

import ru.clementl.metrotimex.converters.toLong
import java.time.LocalTime

// Factors
const val GAP_Q = 0.3
const val ADDING_GAP = 0.35 // Average monthly (GapHours divide TotalHours)

const val MENTOR_Q = 0.15
const val MASTER_Q = 0.1

const val NIGHT_HOURS_Q = 0.4
const val EVENING_HOURS_Q = 0.2

const val FIRST_CLASS_Q = 0.3
const val SECOND_CLASS_Q = 0.2
const val THIRD_CLASS_Q = 0.1
const val NO_CLASS_Q = 0.0

const val STAGE_1_4 = 0.1
const val STAGE_5_9 = 0.15
const val STAGE_10_14 = 0.2
const val STAGE_15_19 = 0.25
const val STAGE_20_PLUS = 0.3

const val NDFL = 0.13
const val UNION_Q = 0.01


// Duration in millis
const val SECOND_MILLI = 1000L
const val MINUTE_MILLI = 60 * SECOND_MILLI
const val HOUR_MILLI = 60 * MINUTE_MILLI
const val DAY_MILLI = 24 * HOUR_MILLI
const val WEEK_MILLI = 7 * DAY_MILLI

// Basic rate per hour in Metropolitan
const val RATE_PER_HOUR_DEFAULT = 390.43
const val RATE_PER_MILLI_DEFAULT = RATE_PER_HOUR_DEFAULT / HOUR_MILLI
const val RESERVE_LIGHT_Q = 0.8929
const val RATE_RESERVE_LIGHT_DEFAULT = 348.60 //  RPH * 0.8929
const val RATE_RESERVE_LIGHT_MILLI_DEFAULT = RATE_RESERVE_LIGHT_DEFAULT / HOUR_MILLI
const val RATE_GAP_DEFAULT = GAP_Q * RATE_PER_HOUR_DEFAULT
const val RATE_GAP_MILLI = RATE_GAP_DEFAULT / HOUR_MILLI

// ATZ constants
const val ATZ_FACTOR = 0.75
const val ATZ_DURATION_MILLI = HOUR_MILLI - 1

const val RATE_ATZ_SHIFT_DEFAULT = ATZ_FACTOR * (RATE_PER_HOUR_DEFAULT - RATE_RESERVE_LIGHT_DEFAULT) + RATE_RESERVE_LIGHT_DEFAULT
const val RATE_ATZ_SHIFT_PER_MILLI_DEFAULT = RATE_ATZ_SHIFT_DEFAULT / HOUR_MILLI



// Updating time
const val UPDATING_DELAY = 16L

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
val DAY_CHANGE_TIME: LocalTime = LocalTime.of(0, 0)
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
val START_POINTS =
    setOf(SHIFT_SP, WEEKEND_SP, SICK_DAY_SP, VACATION_DAY_SP, MEDIC_DAY_SP, UNKNOWN_SP)

// set of all the TimePoints types, which represent END of the interval
val END_POINTS = setOf(SHIFT_EP, WEEKEND_EP, SICK_DAY_EP, VACATION_DAY_EP, MEDIC_DAY_EP, UNKNOWN_EP)

val DAYS_FOR_TONIGHT_BEFORE = 100 // Days before today to load from db for TonightFragment
val DAYS_FOR_TONIGHT_AFTER =
    100 // Days after today to load from db for TonightFragment, today inclusive

// Maximum duration of the Night Gap, millis
val NIGHT_GAP_MAX_DURATION = LocalTime.of(7, 0).toLong()

// Duration of BeforeShiftAdvancedState priority
val BEFORE_SHIFT_STATE_PRIORITY_DURATION = LocalTime.of(2, 0).toLong()

// Duration of AfterShiftAdvancedState priority
val AFTER_SHIFT_STATE_PRIORITY_DURATION = LocalTime.of(0, 30).toLong()




val EVENING_FROM = LocalTime.of(16, 0)
val EVENING_TILL = LocalTime.of(22, 0).minusNanos(1)
val NIGHT_FROM = LocalTime.of(22, 0)
val NIGHT_TILL = LocalTime.of(6, 0).minusNanos(1)

val ALWAYS_NIGHT_AFTER = LocalTime.of(19, 0)

// Time after those odd/even of the day changes
val ODD_EVEN_DIFFER_AFTER = LocalTime.of(22,30)

// Latest night end time
val LATEST_NIGHT_END = LocalTime.of(4, 0)

// Shift is evening only if starts after this time
val EARLIEST_START_OF_EVENING_SHIFT = LocalTime.of(11, 0)

// When duration is more that that, app shows time as "5д 2:05:07" against "122:05:07"
const val MAX_TIME_WITHOUT_DAYS = 3 * DAY_MILLI

