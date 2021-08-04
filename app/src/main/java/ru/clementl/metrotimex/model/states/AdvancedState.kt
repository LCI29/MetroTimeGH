package ru.clementl.metrotimex.model.states

import ru.clementl.metrotimex.*
import ru.clementl.metrotimex.converters.toDate
import ru.clementl.metrotimex.converters.toLong
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.TimeSpan
import ru.clementl.metrotimex.model.data.WorkDayType
import ru.clementl.metrotimex.model.data.compareTo

sealed class AdvancedState(val description: String)

object GapAdvancedState : AdvancedState(GAP_STRING)
object ShiftAdvancedState : AdvancedState(SHIFT_STRING)
object BeforeShiftAdvancedState : AdvancedState(NEXT_SHIFT_STRING)
object AfterShiftAdvancedState : AdvancedState(SHIFT_FINISHED_STRING)
object NoDataAdvancedState : AdvancedState(NO_DATA_STRING)
object NightGapAdvancedState : AdvancedState(NIGHT_GAP_STRING)

abstract class SpecialAdvancedState(val type: WorkDayType) : AdvancedState(type.desc)

object SickAdvancedState : SpecialAdvancedState(WorkDayType.SICK_LIST)
object WeekendAdvancedState : SpecialAdvancedState(WorkDayType.WEEKEND)
object VacationAdvancedState : SpecialAdvancedState(WorkDayType.VACATION_DAY)
object MedicAdvancedState : SpecialAdvancedState(WorkDayType.MEDIC_DAY)


class UnknownAdvancedState constructor(description: String) : AdvancedState(description) {

    companion object {
        fun btw(beforeCode: Int?, afterCode: Int?): UnknownAdvancedState {
            val interval = BetweenPoints(beforeCode, afterCode)
            return UnknownAdvancedState("$interval")
        }

    }
}

fun Long.advancedState(calendar: List<DayStatus>?): AdvancedState {
    if (calendar == null || calendar.isEmpty()) return NoDataAdvancedState
    val interval = getInterval(calendar)
    if (interval.startPoint?.code == SHIFT_EP &&
        (this - interval.startPoint.dateTimeLong) < AFTER_SHIFT_STATE_PRIORITY_DURATION)
            return AfterShiftAdvancedState
    when (interval.simpleState) {
        ShiftSimpleState -> return ShiftAdvancedState
        NightGapSimpleState -> return BeforeShiftAdvancedState
    }
    val dayId = toDate().toLong()
    val currentDay = calendar.find { it.dateLong == dayId } ?: return NoDataAdvancedState
    val workDayType = currentDay.workDayType ?: return UnknownAdvancedState("Нулевой тип дня")
    return when (workDayType) {
        WorkDayType.SHIFT ->
            return when {
                this < currentDay -> BeforeShiftAdvancedState
                this > currentDay -> AfterShiftAdvancedState
                else -> ShiftAdvancedState
            }
        WorkDayType.WEEKEND -> WeekendAdvancedState
        WorkDayType.SICK_LIST -> SickAdvancedState
        WorkDayType.VACATION_DAY -> VacationAdvancedState
        WorkDayType.MEDIC_DAY -> MedicAdvancedState
        WorkDayType.UNKNOWN -> UnknownAdvancedState("Неизвестный тип дня")
    }
}



