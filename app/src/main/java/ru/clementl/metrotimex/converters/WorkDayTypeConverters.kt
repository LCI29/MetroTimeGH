package ru.clementl.metrotimex.converters

import ru.clementl.metrotimex.model.data.*


fun WorkDayType.toInt() = type

fun Int.toWorkDayType(): WorkDayType {
    return when (this) {
        TYPE_SHIFT -> WorkDayType.SHIFT
        TYPE_WEEKEND -> WorkDayType.WEEKEND
        TYPE_SICK_LIST -> WorkDayType.SICK_LIST
        TYPE_VACATION_DAY -> WorkDayType.VACATION_DAY
        TYPE_MEDIC -> WorkDayType.MEDIC_DAY
        TYPE_DONOR_DAY -> WorkDayType.DONOR_DAY
        TYPE_SICK_LIST_CHILD -> WorkDayType.SICK_LIST_CHILD
        else -> WorkDayType.UNKNOWN
    }
}
