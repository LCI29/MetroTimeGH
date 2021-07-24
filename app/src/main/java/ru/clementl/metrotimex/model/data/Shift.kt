package ru.clementl.metrotimex.model.data

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.*
import ru.clementl.metrotimex.converters.toTime
import ru.clementl.metrotimex.converters.toWeekDayType
import ru.clementl.metrotimex.utils.asSimpleTime
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

    @ColumnInfo(name = "name")
    val name: String? = "Смена",

    @ColumnInfo(name = "week_day_type")
    val weekDayTypeString: String?, // Р - рабочий, В - выходной, Н - неизвестный

    @ColumnInfo(name = "odd_even")
    val oddEven: Int?, // 0-без, 1-нечет, 2-чет


    @ColumnInfo(name = "start_time")
    val startTimeInt: Int?,


    @ColumnInfo(name = "start_loc")
    val startLoc: String? = "",

    @ColumnInfo(name = "end_time")
    val endTimeInt: Int?,

    @ColumnInfo(name = "end_loc")
    val endLoc: String? = "",

) {
    @Ignore val startTime = startTimeInt?.toTime()
    @Ignore val endTime = endTimeInt?.toTime()
    @Ignore val weekDayType = weekDayTypeString?.toWeekDayType()
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
    fun getDescriptionString() =
        "${startTime?.asSimpleTime()} ${startLoc?.toUpperCase(Locale.ROOT)}" +
                "${if (startLoc?.isEmpty() != false) "" else " "}-" +
                " ${endTime?.asSimpleTime()} ${endLoc?.toUpperCase(Locale.ROOT)}"

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "$name-${weekDayTypeString}$oddEven" // 85.2-Р0, М18-В2

}

