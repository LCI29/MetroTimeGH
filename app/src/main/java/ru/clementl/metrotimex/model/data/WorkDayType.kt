package ru.clementl.metrotimex.model.data

const val TYPE_UNKNOWN = -1
const val DESC_UNKNOWN = "Неизвестно"
const val TYPE_SHIFT = 0
const val DESC_SHIFT = "Смена"
const val TYPE_WEEKEND = 1
const val DESC_WEEKEND = "Выходной"
const val TYPE_SICK_LIST = 2
const val DESC_SICK_LIST = "Б/Л"
const val TYPE_VACATION_DAY = 3
const val DESC_VACATION_DAY = "Отпуск"
const val TYPE_MEDIC = 4
const val DESC_MEDIC = "Медкомиссия"

enum class WorkDayType(val type: Int, val desc: String) {
    UNKNOWN(TYPE_UNKNOWN, DESC_UNKNOWN),
    SHIFT(TYPE_SHIFT, DESC_SHIFT),
    WEEKEND(TYPE_WEEKEND, DESC_WEEKEND),
    SICK_LIST(TYPE_SICK_LIST, DESC_SICK_LIST),
    VACATION_DAY(TYPE_VACATION_DAY, DESC_VACATION_DAY),
    MEDIC_DAY(TYPE_MEDIC, DESC_MEDIC)
}
