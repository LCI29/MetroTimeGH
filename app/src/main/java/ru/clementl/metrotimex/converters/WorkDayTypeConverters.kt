package ru.clementl.metrotimex.converters

import androidx.room.TypeConverter
import ru.clementl.metrotimex.model.data.*

class WorkDayTypeConverters {
    @TypeConverter
    fun workDayToInt(workDayType: WorkDayType) = workDayType.type

    @TypeConverter
    fun intToWorkDayType(type: Int): WorkDayType {
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