package ru.clementl.metrotimex.model.states

import ru.clementl.metrotimex.*
import ru.clementl.metrotimex.model.data.DayStatus

sealed class SimpleState(val desc: String)

object ShiftSimpleState : SimpleState(SHIFT_STRING)
object GapSimpleState : SimpleState(GAP_STRING)
object SickSimpleState : SimpleState(SICK_DAY_STRING)
object VacationSimpleState : SimpleState(VACATION_DAY_STRING)
object NoDataSimpleState : SimpleState(NO_DATA_STRING)
object NightGapSimpleState : SimpleState(NIGHT_GAP_STRING)

class UnknownSimpleState private constructor(description: String) : SimpleState(description) {

    companion object {
        fun btw(beforeCode: Int?, afterCode: Int?): UnknownSimpleState {
            val interval = BetweenPoints(beforeCode, afterCode)
            return UnknownSimpleState("$interval")
        }

    }
}

data class BetweenPoints(val between: Int?, val and: Int?)

fun Long.simpleState(calendar: List<DayStatus>?): SimpleState {
    calendar?.let {
        return getInterval(it).simpleState
    }
    return NoDataSimpleState
}

fun Interval.simpleState(): SimpleState {
    val beforeCode = startPoint?.code
    val afterCode = endPoint?.code
    val unknown by lazy { UnknownSimpleState.btw(beforeCode, afterCode) }
    if (beforeCode == null && afterCode == null) return NoDataSimpleState
    return when (beforeCode) {

        SHIFT_SP -> when (afterCode) {
            SHIFT_SP -> unknown // Две точки начала смены не могут идти подряд
            in START_POINTS -> ShiftSimpleState

            in END_POINTS -> ShiftSimpleState
            null -> unknown
            else -> unknown
        }

        SHIFT_EP -> when (afterCode) {

            SHIFT_SP -> if (duration!! < NIGHT_GAP_MAX_DURATION) NightGapSimpleState else GapSimpleState
            WEEKEND_SP -> GapSimpleState
            SICK_DAY_SP -> GapSimpleState
            VACATION_DAY_SP -> GapSimpleState
            MEDIC_DAY_SP -> GapSimpleState
            DONOR_DAY_SP -> GapSimpleState
            UNKNOWN_SP -> GapSimpleState

            SHIFT_EP -> unknown
            WEEKEND_EP -> GapSimpleState
            SICK_DAY_EP -> SickSimpleState
            VACATION_DAY_EP -> VacationSimpleState
            MEDIC_DAY_EP -> GapSimpleState
            DONOR_DAY_EP -> GapSimpleState
            UNKNOWN_EP -> GapSimpleState

            null -> NoDataSimpleState
            else -> unknown
        }

        WEEKEND_SP -> when (afterCode) {
            SHIFT_SP -> GapSimpleState

            SHIFT_EP -> ShiftSimpleState
            WEEKEND_EP -> GapSimpleState

            null -> unknown
            else -> unknown
        }

        WEEKEND_EP -> when (afterCode) {
            in START_POINTS -> GapSimpleState

            SHIFT_EP -> ShiftSimpleState

            null -> NoDataSimpleState
            else -> unknown
        }

        SICK_DAY_SP -> when (afterCode) {
            SHIFT_SP -> GapSimpleState // Это маловероятно, но такое будет считаться разрывом

            SHIFT_EP -> ShiftSimpleState
            SICK_DAY_EP -> SickSimpleState

            null -> unknown
            else -> unknown
        }

        SICK_DAY_EP -> when (afterCode) {
            SICK_DAY_SP -> SickSimpleState
            in START_POINTS -> GapSimpleState

            SHIFT_EP -> ShiftSimpleState

            null -> NoDataSimpleState
            else -> unknown
        }

        VACATION_DAY_SP -> when (afterCode) {
            SHIFT_SP -> GapSimpleState

            SHIFT_EP -> ShiftSimpleState
            VACATION_DAY_EP -> VacationSimpleState

            null -> unknown
            else -> unknown
        }

        VACATION_DAY_EP -> when (afterCode) {
            VACATION_DAY_SP -> VacationSimpleState
            in START_POINTS -> GapSimpleState

            SHIFT_EP -> ShiftSimpleState

            null -> NoDataSimpleState
            else -> unknown
        }

        MEDIC_DAY_SP -> when (afterCode) {
            SHIFT_SP -> GapSimpleState

            SHIFT_EP -> ShiftSimpleState
            MEDIC_DAY_EP -> GapSimpleState

            null -> unknown
            else -> unknown
        }

        MEDIC_DAY_EP -> when (afterCode) {
            in START_POINTS -> GapSimpleState

            SHIFT_EP -> ShiftSimpleState

            null -> NoDataSimpleState
            else -> unknown
        }

        DONOR_DAY_SP -> when (afterCode) {
            SHIFT_SP -> GapSimpleState

            SHIFT_EP -> ShiftSimpleState
            DONOR_DAY_EP -> GapSimpleState

            null -> unknown
            else -> unknown
        }

        DONOR_DAY_EP -> when (afterCode) {
            in START_POINTS -> GapSimpleState

            SHIFT_EP -> ShiftSimpleState

            null -> NoDataSimpleState
            else -> unknown
        }

        UNKNOWN_SP -> when (afterCode) {
            SHIFT_SP -> GapSimpleState

            SHIFT_EP -> ShiftSimpleState

            null -> unknown
            else -> unknown
        }

        UNKNOWN_EP -> when (afterCode) {
            in START_POINTS
            -> GapSimpleState

            null -> NoDataSimpleState
            else -> unknown
        }

        null -> when (afterCode) {
            in START_POINTS
            -> GapSimpleState

            null -> NoDataSimpleState
            else -> unknown
        }

        else -> unknown
    }
}