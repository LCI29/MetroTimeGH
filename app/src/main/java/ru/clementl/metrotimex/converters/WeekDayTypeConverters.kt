package ru.clementl.metrotimex.converters

import androidx.room.TypeConverter
import ru.clementl.metrotimex.model.data.WeekDayType

class WeekDayTypeConverters {

    @TypeConverter
    fun fromWeekDayToString(weekDayType: WeekDayType) = weekDayType.code

    @TypeConverter
    fun fromStringToWeekDayType(s: String): WeekDayType {
        return when (s.toUpperCase()) {
            "Р" -> WeekDayType.WORKDAY
            "В" -> WeekDayType.WEEKEND
            else -> WeekDayType.UNKNOWN
        }
    }
}