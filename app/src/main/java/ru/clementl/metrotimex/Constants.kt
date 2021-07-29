package ru.clementl.metrotimex

import java.time.LocalTime

const val RATE_PER_HOUR = 375.41

const val SHIFT_CREATING = 1000
const val SHIFT_EDITING = 2000
const val NO_DAY_ID = -1L

const val DEFAULT_START_HOUR = 8
const val DEFAULT_END_HOUR = 16
const val DEFAULT_MINUTE = 0

// SP = Start Point. EP = End Point.
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

// Константы описаний
val SHIFT_STRING = "Смена"
val WEEKEND_STRING = "Выходной"
val SICK_DAY_STRING = "Больничный"
val SICK_DAY_STRING_SHORT = "Б/Л"
val VACATION_DAY_STRING = "Отпуск"
val MEDIC_DAY = "Медкомиссия"

val GAP_STRING = "Разрыв"

val NO_DATA_STRING = "Нет данных"

val START_POINTS = setOf(SHIFT_SP, WEEKEND_SP, SICK_DAY_SP, VACATION_DAY_SP, MEDIC_DAY_SP, UNKNOWN_SP)
val END_POINTS = setOf(SHIFT_EP, WEEKEND_EP, SICK_DAY_EP, VACATION_DAY_EP, MEDIC_DAY_EP, UNKNOWN_EP)