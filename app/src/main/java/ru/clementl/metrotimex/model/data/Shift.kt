package ru.clementl.metrotimex.model.data

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.clementl.metrotimex.converters.DateTimeConverters
import ru.clementl.metrotimex.converters.ShiftConverter
import ru.clementl.metrotimex.converters.WeekDayTypeConverters
import java.time.LocalTime

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

@Entity(tableName = "shifts")
@TypeConverters(ShiftConverter::class)
data class Shift(

    @ColumnInfo(name = "name")
    val name: String? = "Смена",

    @ColumnInfo(
        name = "week_day_type",
        typeAffinity = ColumnInfo.TEXT
    ) val weekDayType: WeekDayType?, // Р - рабочий, В - выходной, Н - неизвестный

    @ColumnInfo(name = "odd_even")
    val oddEven: Int?, // 0-без, 1-нечет, 2-чет


    @ColumnInfo(
        name = "start_time",
        typeAffinity = ColumnInfo.INTEGER)
    val startTime: LocalTime?,


    @ColumnInfo(name = "start_loc") val startLoc: String? = "",

    @ColumnInfo(
        name = "end_time",
        typeAffinity = ColumnInfo.INTEGER
    ) val endTime: LocalTime?,

    @Nullable @ColumnInfo(name = "end_loc") val endLoc: String? = "",

) {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "$name-${weekDayType?.code}$oddEven" // 85.2-Р0, М18-В2
    init {
        id = ""
    }
}

