package ru.clementl.metrotimex.model.norma

import ru.clementl.metrotimex.HOLIDAYS_2021
import ru.clementl.metrotimex.NORMA_MAP
import ru.clementl.metrotimex.converters.toDate
import ru.clementl.metrotimex.converters.toLong
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.TimeSpan
import ru.clementl.metrotimex.model.data.WorkDayType.*
import java.time.*

data class WorkMonth(val yearMonth: YearMonth) : TimeSpan(
    yearMonth.atDay(1).atStartOfDay().toLong(),
    yearMonth.atEndOfMonth().atTime(LocalTime.MAX).toLong()
) {

    val standardNormaHours = NORMA_MAP[yearMonth]

    val sundaysAndHolidaysCount = countSundaysAndHolidays()
    private fun countSundaysAndHolidays(): Int {
        var sundays = 0
        for (i in 1..yearMonth.atEndOfMonth().dayOfMonth) {
            val day = yearMonth.atDay(i)
            if (day.dayOfWeek == DayOfWeek.SUNDAY ||
                    HOLIDAYS_2021.contains(day)) sundays++
        }
        return sundays
    }

    val standardNormaDays = yearMonth.lengthOfMonth() - sundaysAndHolidaysCount

    val standardNormaWeekend = sundaysAndHolidaysCount

    fun allDays(calendar: List<DayStatus>): List<DayStatus> {
        return calendar.filter { YearMonth.from(it.date) == this.yearMonth }
    }

    fun allShifts(calendar: List<DayStatus>): List<DayStatus> {
        return allDays(calendar).filter { it.shift != null }
    }

    fun workedInMillis(calendar: List<DayStatus>): Long {
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

    fun realNormaDays(calendar: List<DayStatus>): Int {
        return standardNormaDays - allDays(calendar).filter {
                    it.workDayType in setOf(MEDIC_DAY, SICK_LIST, VACATION_DAY) && it.date.dayOfWeek != DayOfWeek.SUNDAY
        }.count()
    }

    fun realNormaHours(calendar: List<DayStatus>): Double? {
        standardNormaHours?.let {
            val q = realNormaDays(calendar).toDouble() / standardNormaDays
            return q * it
        }
        return null
    }


    companion object {
        fun of(milli: Long): WorkMonth {
            return WorkMonth(YearMonth.from(milli.toDate()))
        }

        fun of(date: LocalDate): WorkMonth {
            return WorkMonth(YearMonth.from(date))
        }

        fun of(dateTime: LocalDateTime): WorkMonth {
            return WorkMonth(YearMonth.from(dateTime))
        }

        fun of(yearMonth: YearMonth): WorkMonth {
            return WorkMonth(yearMonth)
        }

        fun of(year: Int, month: Int): WorkMonth {
            return WorkMonth(YearMonth.of(year, month))
        }
    }
}
