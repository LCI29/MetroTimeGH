package ru.clementl.metrotimex.model.data

import ru.clementl.metrotimex.*

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
const val TYPE_DONOR_DAY = 5
const val DESC_DONOR_DAY = "Донорский день"
const val TYPE_SICK_LIST_CHILD = 6
const val DESC_SICK_LIST_CHILD = "Б/Л по уходу"

enum class WorkDayType(val type: Int, val desc: String, val startPointCode: Int, val endPointCode: Int) {
    UNKNOWN(TYPE_UNKNOWN, DESC_UNKNOWN, UNKNOWN_SP, UNKNOWN_EP),
    SHIFT(TYPE_SHIFT, DESC_SHIFT, SHIFT_SP, SHIFT_EP),
    WEEKEND(TYPE_WEEKEND, DESC_WEEKEND, WEEKEND_SP, WEEKEND_EP),
    SICK_LIST(TYPE_SICK_LIST, DESC_SICK_LIST, SICK_DAY_SP, SICK_DAY_EP),
    VACATION_DAY(TYPE_VACATION_DAY, DESC_VACATION_DAY, VACATION_DAY_SP, VACATION_DAY_EP),
    MEDIC_DAY(TYPE_MEDIC, DESC_MEDIC, MEDIC_DAY_SP, MEDIC_DAY_EP),
    DONOR_DAY(TYPE_DONOR_DAY, DESC_DONOR_DAY, DONOR_DAY_SP, DONOR_DAY_EP),
    SICK_LIST_CHILD(TYPE_SICK_LIST_CHILD, DESC_SICK_LIST_CHILD, SICK_DAY_CHILD_SP, SICK_DAY_CHILD_EP)
}
