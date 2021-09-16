package ru.clementl.metrotimex.model.data

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.*
import ru.clementl.metrotimex.converters.toInt
import ru.clementl.metrotimex.converters.toTime
import ru.clementl.metrotimex.converters.toWeekDayType
import ru.clementl.metrotimex.model.states.TimePoint
import ru.clementl.metrotimex.utils.asSimpleTime
import ru.clementl.metrotimex.utils.ofPattern
import java.time.LocalTime
import java.util.*

//@Entity
//class Shift(
//    _startDate: LocalDate,
//    @ColumnInfo(name = "start") val startTime: LocalTime,
//    @ColumnInfo(name = "end") val endTime: LocalTime,
//    @ColumnInfo(name = "name", defaultValue = "") val shiftName: String = "Смена",
//    @ColumnInfo(name = "start_place", defaultValue = "") val startPlace: String = "",
//    @ColumnInfo(name = "end_place", defaultValue = "") val endPlace: String = ""
//) {
//    @Ignore val date = _startDate
//    @ColumnInfo(name = "day_type") val dayType: DayType = _startDate.getDateType()
//    val endDate: LocalDate
//        get() = if (endTime.isAfter(startTime)) date else date.plusDays(1)
//    val startDateTime: LocalDateTime
//        get() = LocalDateTime.of(date, startTime)
//    val endDateTime: LocalDateTime
//        get() = LocalDateTime.of(endDate, endTime)
//    val duration: Duration
//        get() = Duration.between(startDateTime, endDateTime)
//}

@Entity(tableName = "shifts_table")
data class Shift(

    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    val name: String? = "Смена",

    @ColumnInfo(name = "week_day_type", typeAffinity = ColumnInfo.TEXT)
    val weekDayTypeString: String? = "Н", // Р - рабочий, В - выходной, Н - неизвестный

    @ColumnInfo(name = "odd_even", typeAffinity = ColumnInfo.INTEGER)
    val oddEven: Int? = 0, // 0-без, 1-нечет, 2-чет


    @ColumnInfo(name = "start_time", typeAffinity = ColumnInfo.INTEGER)
    val startTimeInt: Int?,


    @ColumnInfo(name = "start_loc", typeAffinity = ColumnInfo.TEXT)
    val startLoc: String? = "",

    @ColumnInfo(name = "end_time", typeAffinity = ColumnInfo.INTEGER)
    val endTimeInt: Int?,

    @ColumnInfo(name = "end_loc", typeAffinity = ColumnInfo.TEXT)
    val endLoc: String? = "",

    @ColumnInfo(name = "is_reserve", typeAffinity = ColumnInfo.INTEGER)
    val isReserveInt: Int? = 0,

    @ColumnInfo(name = "has_atz", typeAffinity = ColumnInfo.INTEGER)
    val hasAtzInt: Int? = 0

) {
    @Ignore val startTime = startTimeInt?.toTime()
    @Ignore val endTime = endTimeInt?.toTime()
    @Ignore val weekDayType = weekDayTypeString?.toWeekDayType()
    @Ignore val isReserve: Boolean? = if (isReserveInt == null) null else isReserveInt != 0
    @Ignore val hasAtz: Boolean? = if (hasAtzInt == null) null else hasAtzInt != 0
    val oddEvenString: String
        get() = oddEven?.let {
            when(it) {
                1 -> "неч"
                2 -> "чет"
                else -> ""
            }
        } ?: ""
    /**
     * Returns string like "8:25 СК - 16:04 СК"
     */
    fun getDescriptionString(withName: Boolean = false) =
        (if (withName) "$name " else "") +
                "${startTime?.ofPattern("H:mm")} ${startLoc?.toUpperCase(Locale.ROOT)}" +
                "${if (startLoc?.isEmpty() != false) "" else " "}-" +
                " ${endTime?.ofPattern("H:mm")} ${endLoc?.toUpperCase(Locale.ROOT)}"

    @PrimaryKey
    @ColumnInfo(name = "shift_id")
    var id: String = "$name-${weekDayTypeString}$oddEven" // 85.2-Р0, М18-В2

    companion object {
        fun of(
            startHours: Int,
            startMinutes: Int,
            endHours: Int,
            endMinutes: Int,
            reserve: Boolean = false,
            atz: Boolean = false
        ): Shift {
            return Shift(
                startTimeInt = LocalTime.of(startHours, startMinutes).toInt(),
                endTimeInt = LocalTime.of(endHours, endMinutes).toInt(),
                isReserveInt = if (reserve) 1 else 0,
                hasAtzInt = if (atz) 1 else 0
            )
        }
    }

}

