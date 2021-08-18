package ru.clementl.metrotimex.model.data

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.*
import ru.clementl.metrotimex.DAY_START_TIME
import ru.clementl.metrotimex.converters.*
import ru.clementl.metrotimex.model.states.*
import ru.clementl.metrotimex.utils.asSimpleDate
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
    val shift: Shift?
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


