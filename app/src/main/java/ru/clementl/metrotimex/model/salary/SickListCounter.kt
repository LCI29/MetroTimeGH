package ru.clementl.metrotimex.model.salary

import ru.clementl.metrotimex.*
import ru.clementl.metrotimex.converters.toLong
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.MachinistStatus
import ru.clementl.metrotimex.model.data.WorkDayType
import ru.clementl.metrotimex.model.data.getSickListQ
import ru.clementl.metrotimex.model.norma.WorkMonth
import ru.clementl.metrotimex.utils.logd
import java.time.LocalDate

const val SICK_LIST_NORMAL = 1
const val SICK_LIST_CHILD = 9

class SickListCounter() {

    fun count(date: LocalDate, machinistStatus: MachinistStatus, sickListType: WorkDayType?): Double {
        if (sickListType !in setOf(WorkDayType.SICK_LIST, WorkDayType.SICK_LIST_CHILD)) return 0.0
        val year = YEAR_CONSTS.find { it.year.value == date.year }
        return when (sickListType) {
            WorkDayType.SICK_LIST_CHILD -> year?.sickDayMaxPay ?: 0.0
            WorkDayType.SICK_LIST ->
                return when (machinistStatus.machinist.getSickListQ(date.toLong())) {
                    SICK_Q_MAX, SICK_Q_UNDER_8_YEARS, SICK_Q_UNDER_5_YEARS -> year?.sickDayMaxPay ?: 0.0
                    SICK_Q_UNDER_6_MONTHS -> year?.mrot?.div(date.lengthOfMonth()) ?: 0.0
                    else -> 0.0
                }
            else -> 0.0
        }
    }
}