package ru.clementl.metrotimex.model.data

import ru.clementl.metrotimex.FIRST_CLASS_Q
import ru.clementl.metrotimex.cnst.*
import ru.clementl.metrotimex.*
import ru.clementl.metrotimex.converters.toDate
import ru.clementl.metrotimex.converters.toLong
import java.time.*

data class Machinist(
    val onPostSince: LocalDate = LocalDate.now().minusDays(1),
    val qualificationClass: Int = 4,
    val isMaster: Boolean = false,
    val isMentor: Boolean = false,
    val monthBonus: Double = 0.25,
    val isInUnion: Boolean = true
) {
    fun stageOn(moment: Long): Period {
        return Period.between(onPostSince, moment.toDate())
    }
}

fun Machinist.getClassQ(): Double {
    return when (qualificationClass) {
        1 -> FIRST_CLASS_Q
        2 -> SECOND_CLASS_Q
        3 -> THIRD_CLASS_Q
        else -> NO_CLASS_Q
    }
}

fun Machinist.getStageQ(moment: Long): Double {
    return when (stageOn(moment).years) {
        in Int.MIN_VALUE..0 -> 0.0
        in 1..4 -> STAGE_1_4
        in 5..9 -> STAGE_5_9
        in 10..14 -> STAGE_10_14
        in 15..19 -> STAGE_15_19
        else -> STAGE_20_PLUS
    }
}

fun Machinist.getYearlyBonusQ(): Double {
    val fullYears =
        stageOn(LocalDateTime.now().withMonth(12).withDayOfMonth(31).toLong()).years
    return when (fullYears) {
        in Int.MIN_VALUE..0 -> YEARLY_13_Q_0
        1 -> YEARLY_13_Q_1
        2 -> YEARLY_13_Q_2
        3 -> YEARLY_13_Q_3
        4, 5 -> YEARLY_13_Q_4_5
        in 6..9 -> YEARLY_13_Q_6_9
        in 10..Int.MAX_VALUE -> YEARLY_13_Q_10_PLUS
        else -> 0.0
    }
}

fun Machinist.getMasterQ() = if (isMaster) MASTER_Q else 0.0


fun Machinist.getMentorQ() = if (isMentor) MENTOR_Q else 0.0

fun Machinist.getUnionQ() = if (isInUnion) UNION_Q else 0.0

fun Machinist.getSickListQ(moment: Long): Double {
    return when (stageOn(moment).months) {
        in Int.MIN_VALUE..5 -> SICK_Q_UNDER_6_MONTHS
        in 6..59 -> SICK_Q_UNDER_5_YEARS
        in 60..95 -> SICK_Q_UNDER_8_YEARS
        in 96..Int.MAX_VALUE -> SICK_Q_MAX
        else -> SICK_Q_UNDER_6_MONTHS
    }
}






