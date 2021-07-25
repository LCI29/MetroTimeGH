package ru.clementl.metrotimex.model.data

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.*
import ru.clementl.metrotimex.converters.*
import ru.clementl.metrotimex.utils.asSimpleDate
import java.time.LocalDate
import java.time.LocalDateTime


//sealed class DayStatus (val date: LocalDate) {abstract val type: Int}
//
//
//
//open class Workday(date: LocalDate, val shift: Shift) : DayStatus(date) {override val type = TYPE_SHIFT}
//
//class Weekend(date: LocalDate) : DayStatus(date) { override val type = TYPE_WEEKEND }
//
//class SickListDay(date: LocalDate) : DayStatus(date) { override val type = TYPE_SICK_LIST }
//
//class VacationDay(date: LocalDate) : DayStatus(date) { override val type = TYPE_VACATION_DAY }
//
//class UnknownDay(date: LocalDate) : DayStatus(date) { override val type  = TYPE_UNKNOWN }
//
//class MedicDay(date: LocalDate) : DayStatus(date) {override val type = TYPE_MEDIC}

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
