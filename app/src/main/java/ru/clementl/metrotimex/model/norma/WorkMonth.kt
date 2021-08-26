package ru.clementl.metrotimex.model.norma

import ru.clementl.metrotimex.CHANGED_WEEKENDS_2021
import ru.clementl.metrotimex.HOLIDAYS_2021
import ru.clementl.metrotimex.HOUR_MILLI
import ru.clementl.metrotimex.NORMA_MAP
import ru.clementl.metrotimex.converters.toDate
import ru.clementl.metrotimex.converters.toLong
import ru.clementl.metrotimex.model.data.*
import ru.clementl.metrotimex.model.data.WorkDayType.*
import ru.clementl.metrotimex.utils.asShortString
import ru.clementl.metrotimex.utils.asShortString
import ru.clementl.metrotimex.utils.inFloatHours
import java.time.*
import kotlin.math.roundToInt

data class WorkMonth(val yearMonth: YearMonth, val calendar: List<DayStatus>) : TimeSpan(
    yearMonth.atDay(1).atStartOfDay().toLong(),
    yearMonth.atEndOfMonth().atTime(LocalTime.MAX).toLong()
) {

    val standardNormaHours = NORMA_MAP[yearMonth]

    val listOfDays: List<DayStatus> = calendar.filter { YearMonth.from(it.date) == this.yearMonth }.sortedBy { it.date }

    val allShifts: List<DayStatus> = listOfDays.filter { it.shift != null }

    val sundaysAndHolidaysCount = countSundaysAndHolidays()
    private fun countSundaysAndHolidays(): Int {
        var sundays = 0
        for (i in 1..yearMonth.atEndOfMonth().dayOfMonth) {
            val day = yearMonth.atDay(i)
            if (day.dayOfWeek ==
                DayOfWeek.SUNDAY || HOLIDAYS_2021.contains(day) || CHANGED_WEEKENDS_2021.contains(
                    day
                )
            )
                sundays++
        }
        return sundays
    }

    val standardNormaDays = yearMonth.lengthOfMonth() - sundaysAndHolidaysCount
    val standardNormaWeekend = sundaysAndHolidaysCount

    val realNormaDays: Int
        get() {
            return standardNormaDays - listOfDays.count {
                it.workDayType in setOf(
                    MEDIC_DAY,
                    SICK_LIST,
                    VACATION_DAY
                ) && it.date.dayOfWeek != DayOfWeek.SUNDAY
            }
        }

    // Returns HOURS in Double
    val realNormaHours: Double?
        get() {
        standardNormaHours?.let {
            val q = realNormaDays.toDouble() / standardNormaDays
            return q * it
        }
        return null
    }

    val realNormaWeekend: Int = standardNormaWeekend - listOfDays.count {
        it.workDayType in setOf(
            MEDIC_DAY,
            SICK_LIST,
            VACATION_DAY
        ) && it.date.dayOfWeek == DayOfWeek.SUNDAY
    }

    val workedInHours: Double
        get() = Duration.ofMillis(workedInMillis()).toMillis().toDouble() / HOUR_MILLI

    val normaString: String
        get() =
            "${workedInHours.inFloatHours(false)} / ${realNormaHours?.inFloatHours(false)}"

    val weekendString: String
        get() = "${countOf(WEEKEND)} / ${realNormaWeekend}"

    val workdayString: String
        get() = "${countOf(SHIFT)} / $realNormaDays"

    val nightShifts = listOfDays.count { it.isNightShift(calendar) }

    val normaProgress = ((workedInHours.toFloat() / (realNormaHours?.toFloat() ?: Float.MAX_VALUE)) * 100)





    fun countOf(workDayType: WorkDayType): Int = listOfDays.count { it.isA(workDayType) }

    fun previous(): WorkMonth {
        return of(yearMonth.minusMonths(1), calendar)
    }

    fun next(): WorkMonth {
        return of(yearMonth.plusMonths(1), calendar)
    }



    fun workedInMillis(): Long {
        return calendar
            .filter { containsOrNear(it, 3) }
            .sumOf { it.shiftTimeSpan?.intersect(this)?.duration ?: 0 }
    }


    private fun containsOrNear(day: DayStatus?, rangeOut: Long): Boolean {
        day?.let {
            return (YearMonth.from(it.date) == yearMonth) ||
                    (YearMonth.from(it.date.plusDays(rangeOut)) == yearMonth) ||
                    (YearMonth.from(it.date.minusDays(rangeOut)) == yearMonth)
        }
        return false
    }





    val asString = yearMonth.asShortString()

    companion object {
        fun of(milli: Long, calendar: List<DayStatus> = listOf()): WorkMonth {
            return WorkMonth(YearMonth.from(milli.toDate()), calendar)
        }

        fun of(date: LocalDate, calendar: List<DayStatus> = listOf()): WorkMonth {
            return WorkMonth(YearMonth.from(date), calendar)
        }

        fun of(dateTime: LocalDateTime, calendar: List<DayStatus> = listOf()): WorkMonth {
            return WorkMonth(YearMonth.from(dateTime), calendar)
        }

        fun of(yearMonth: YearMonth, calendar: List<DayStatus> = listOf()): WorkMonth {
            return WorkMonth(yearMonth, calendar)
        }

        fun of(year: Int, month: Int, calendar: List<DayStatus> = listOf()): WorkMonth {
            return WorkMonth(YearMonth.of(year, month), calendar)
        }
    }
}
