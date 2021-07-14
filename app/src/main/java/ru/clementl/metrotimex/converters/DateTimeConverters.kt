package ru.clementl.metrotimex.converters

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

class DateTimeConverters {
    @TypeConverter
    fun fromDateToLong(date: LocalDate): Long {
        return date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    @TypeConverter
    fun fromLongToDate(millis: Long): LocalDate {
        return LocalDateTime.ofInstant(Date(millis).toInstant(), ZoneId.systemDefault()).toLocalDate()
    }

    @TypeConverter
    fun fromTimeToInt(time: LocalTime) = time.toSecondOfDay()

    @TypeConverter
    fun fromIntToTime(secondsOfDay: Int) = LocalTime.ofSecondOfDay(secondsOfDay.toLong())
}


