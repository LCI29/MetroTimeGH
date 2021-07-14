package ru.clementl.metrotimex.converters

import androidx.room.TypeConverter
import ru.clementl.metrotimex.model.data.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

class ShiftConverter {

    @TypeConverter
    fun fromLocalTime(time: LocalTime?): Int = time?.toSecondOfDay() ?: 0

    @TypeConverter
    fun toLocalTime(seconds: Int): LocalTime = LocalTime.ofSecondOfDay(seconds.toLong())

    @TypeConverter
    fun fromDate(date: LocalDate?): Long {
        return date?.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli() ?: 0
    }

    @TypeConverter
    fun toDate(millis: Long): LocalDate {
        return LocalDateTime.ofInstant(Date(millis).toInstant(), ZoneId.systemDefault()).toLocalDate()
    }

    @TypeConverter
    fun fromWeekDayType(weekDayType: WeekDayType) = weekDayType.code

    @TypeConverter
    fun toWeekDayType(s: String): WeekDayType {
        return when (s.toUpperCase()) {
            "Р" -> WeekDayType.WORKDAY
            "В" -> WeekDayType.WEEKEND
            else -> WeekDayType.UNKNOWN
        }
    }

    @TypeConverter
    fun fromWorkDayType(workDayType: WorkDayType?): Int = workDayType?.type ?: WorkDayType.UNKNOWN.type

    @TypeConverter
    fun toWorkDayType(type: Int): WorkDayType {
        return when (type) {
            TYPE_SHIFT -> WorkDayType.SHIFT
            TYPE_WEEKEND -> WorkDayType.WEEKEND
            TYPE_SICK_LIST -> WorkDayType.SICK_LIST
            TYPE_VACATION_DAY -> WorkDayType.VACATION_DAY
            TYPE_MEDIC -> WorkDayType.MEDIC_DAY
            else -> WorkDayType.UNKNOWN
        }
    }


}