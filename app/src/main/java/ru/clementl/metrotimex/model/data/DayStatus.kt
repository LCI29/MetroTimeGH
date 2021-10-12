package ru.clementl.metrotimex.model.data

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.*
import ru.clementl.metrotimex.ALWAYS_NIGHT_AFTER
import ru.clementl.metrotimex.DAY_START_TIME
import ru.clementl.metrotimex.EARLIEST_START_OF_EVENING_SHIFT
import ru.clementl.metrotimex.converters.*
import ru.clementl.metrotimex.model.salary.MachinistSalaryCounter
import ru.clementl.metrotimex.model.states.*
import ru.clementl.metrotimex.utils.asSimpleDate
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Represents a day by its date, type (workday or weekend) and shift if it has
 */
@Entity(tableName = "calendar_table")
data class DayStatus(

    @PrimaryKey @NonNull @ColumnInfo(name = "date")
    val dateLong: Long,

    @ColumnInfo(name = "type", typeAffinity = ColumnInfo.INTEGER)
    val workDayTypeInt: Int?,

    @Nullable @Embedded
    val shift: Shift?,

    @ColumnInfo(name = "day_notes", typeAffinity = ColumnInfo.TEXT)
    val notes: String? = null

) {
    @Ignore val date = dateLong.toDate()
    @Ignore val workDayType = workDayTypeInt?.toWorkDayType()

    val startDateTime: LocalDateTime
        get() {
            when(workDayType) {
                WorkDayType.SHIFT -> return shiftStart()!!
                else -> return LocalDateTime.of(date, DAY_START_TIME)
            }
        }

    val endDateTime: LocalDateTime
        get() {
            when(workDayType) {
                WorkDayType.SHIFT -> return shiftEnd()!!
                else -> return LocalDateTime.of(date.plusDays(1), DAY_START_TIME)
            }
        }

    val startPoint: TimePoint
        get() = TimePoint(startDateTime.toLong(), workDayType?.startPointCode ?: WorkDayType.UNKNOWN.startPointCode)

    val endPoint: TimePoint
        get() = TimePoint(endDateTime.toLong() - 1, workDayType?.endPointCode ?: WorkDayType.UNKNOWN.endPointCode)

    val timeSpan: TimeSpan
        get() = TimeSpan(startPoint.milli, endPoint.milli)

    val shiftTimeSpan: TimeSpan?
        get() = if (workDayType == WorkDayType.SHIFT) timeSpan else null

    val duration: Long
        get() = timeSpan.duration ?: 0

    companion object {
        fun weekendOf(date: LocalDate): DayStatus {
            return DayStatus(
                date.toLong(),
                WorkDayType.WEEKEND.type,
                null
            )
        }

        fun workdayOf(
            date: LocalDate,
            startHour: Int,
            startMinute: Int,
            endHour: Int,
            endMinute: Int,
            reserve: Boolean = false,
            atz: Boolean = false
        ): DayStatus {
            return DayStatus(
                date.toLong(), WorkDayType.SHIFT.type, Shift.of(startHour, startMinute, endHour, endMinute, reserve, atz)
            )
        }

        fun medicDayOf(date: LocalDate): DayStatus {
            return DayStatus(date.toLong(), WorkDayType.MEDIC_DAY.type, null)
        }

        fun vacationDayOf(date: LocalDate): DayStatus {
            return DayStatus(date.toLong(), WorkDayType.VACATION_DAY.type, null)
        }

        fun sickListDayOf(date: LocalDate): DayStatus {
            return DayStatus(date.toLong(), WorkDayType.SICK_LIST.type, null)
        }
    }

}

/**
 * Returns date and time of shift's START if DayStatus object has type [WorkDayType.SHIFT]
 */
fun DayStatus.shiftStart(): LocalDateTime? {
    return if (workDayTypeInt == WorkDayType.SHIFT.type) {
        LocalDateTime.of(date, shift!!.startTime)
    } else null
}

/**
 * Returns date and time of shift's END if DayStatus object has type [WorkDayType.SHIFT]
 */
fun DayStatus.shiftEnd(): LocalDateTime? {
    return if (workDayType == WorkDayType.SHIFT) {
        with(shift!!) {
            if (startTime!!.isBefore(endTime)) LocalDateTime.of(date, endTime)
            else LocalDateTime.of(date.plusDays(1), endTime)
        }
    } else null
}

val DayStatus.descriptionString
    get() = "${date.asSimpleDate(false)}: ${shift?.getDescriptionString(true) ?: workDayType?.desc}"

// Returns the nearest future DayStatus with WorkDayType of SHIFT, if there is not returns null
fun Long.getNextShift(calendar: List<DayStatus>): DayStatus? {
    return calendar.sortedBy { it.date }
        .find { it.workDayType == WorkDayType.SHIFT && it.startPoint.milli > this }
}

fun Long.getCurrentShift(calendar: List<DayStatus>): DayStatus? {
    if (simpleState(calendar) != ShiftSimpleState) return null
    return calendar.sortedBy { it.date }
        .findLast { it.workDayType == WorkDayType.SHIFT && it.startPoint.milli < this}
}

fun Long.getCurrentDayStatus(calendar: List<DayStatus>): DayStatus? {
    return when {
        simpleState(calendar) is ShiftSimpleState -> getCurrentShift(calendar)
        advancedState(calendar) is BeforeShiftAdvancedState -> {
            calendar.sortedBy { it.date }
                .find { it.startPoint.milli > this }
        }
        else -> {
            calendar.sortedBy { it.date }
                .findLast { it.startPoint.milli < this }
        }
    }
}

fun DayStatus.finalSalary(machinistStatus: MachinistStatus): Double {
    if (isShift()) {
        return MachinistSalaryCounter(machinistStatus, this).getFinalSalary()
    } else return 0.0
}

fun DayStatus.isA(workDayType: WorkDayType) = this.workDayType == workDayType
fun DayStatus.isShift(): Boolean = (workDayType == WorkDayType.SHIFT && shift != null)

fun DayStatus.isNightShift(calendar: List<DayStatus>): Boolean {
    return (endPoint.milli + 5).getInterval(calendar).simpleState == NightGapSimpleState ||
            startDateTime.isAfter(LocalDateTime.of(date, ALWAYS_NIGHT_AFTER))
}

fun DayStatus.isEveningShift(calendar: List<DayStatus>): Boolean {
    return ((!isNightShift(calendar)) &&
            startDateTime.isAfter(LocalDateTime.of(date, EARLIEST_START_OF_EVENING_SHIFT)))
}

fun DayStatus.isFinishedShift() = isShift() && timeSpan.isFinished()

fun DayStatus.asMentor(statusChangeList: List<MachinistStatus>) = dateLong.asMentor(statusChangeList)

fun DayStatus.asMaster(statusChangeList: List<MachinistStatus>) = dateLong.asMaster(statusChangeList)

fun DayStatus.eveningSpan(): TimeSpan? {
    if (!isShift()) return null
    return timeSpan.intersect(TimeSpan.eveningOf(date))
}

fun DayStatus.nightSpans(): List<TimeSpan?> {
    if (!isShift()) return listOf(null, null)
    return listOf(
        timeSpan.intersect(TimeSpan.nightEarlyOf(date)),
        timeSpan.intersect(TimeSpan.nightLateOf(date))
    )
}

fun DayStatus.getClassQ(statusChangeList: List<MachinistStatus>) = dateLong.getClassQ(statusChangeList)

fun DayStatus.getStageQ(statusChangeList: List<MachinistStatus>) = dateLong.getStageQ(statusChangeList)

fun DayStatus.getUnionQ(statusChangeList: List<MachinistStatus>) = dateLong.getUnionQ(statusChangeList)


