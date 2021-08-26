package ru.clementl.metrotimex.model.norma

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import ru.clementl.metrotimex.model.data.DayStatus.Companion.medicDayOf
import ru.clementl.metrotimex.model.data.DayStatus.Companion.sickListDayOf
import ru.clementl.metrotimex.model.data.DayStatus.Companion.weekendOf
import ru.clementl.metrotimex.model.data.DayStatus.Companion.workdayOf
import ru.clementl.metrotimex.model.data.WorkDayType.*
import java.time.LocalDate

internal class WorkMonthTest {

    val calendar = listOf(
        workdayOf(LocalDate.of(2021, 7, 27), 16, 0, 17, 0),
        workdayOf(LocalDate.of(2021, 7, 1), 16, 0, 17, 0),
        weekendOf(LocalDate.of(2021, 7, 20)),
        workdayOf(LocalDate.of(2021, 7, 31), 23, 0, 1, 0),
        workdayOf(LocalDate.of(2021, 6, 30), 23, 0, 1, 0),
        sickListDayOf(LocalDate.of(2021, 7, 2)),
        sickListDayOf(LocalDate.of(2021, 7, 3)),
        sickListDayOf(LocalDate.of(2021, 7, 4)),
        medicDayOf(LocalDate.of(2021, 7, 12)),
        workdayOf(LocalDate.of(2021, 5, 19), 15, 0, 16, 0)
    )

    val m1 = WorkMonth.of(2021, 7, calendar)

    fun printStats() {
        println(m1.standardNormaHours)
        println(m1.standardNormaDays)
        println(m1.realNormaDays)
        println(m1.realNormaHours)
        println(m1.workedInHours)
    }


    @Test
    fun workedInMillis() {
        assertEquals(3600 * 4 * 1000L - 4, m1.workedInMillis())
        assertEquals(3, m1.allShifts.count())
        assertEquals(8, m1.listOfDays.count())
    }

    @Test
    fun getSundaysCount() {
        assertEquals(4, m1.sundaysAndHolidaysCount)
    }

    @Test
    fun getStandardNormaDays() {
        assertEquals(27, m1.standardNormaDays)

        val m2 = WorkMonth.of(2021, 12, calendar)
        assertEquals(26, m2.standardNormaDays)
    }

    @Test
    fun realNormaDays() {
        assertEquals(24, m1.realNormaDays)
    }

    @Test
    fun countOf() {
        assertEquals(3, m1.countOf(SHIFT))
        assertEquals(3, m1.countOf(SICK_LIST))
        assertEquals(1, m1.countOf(WEEKEND))
        assertEquals(1, m1.countOf(MEDIC_DAY))
    }

    @Test
    fun getRealNormaWeekend() {
        assertEquals(3, m1.realNormaWeekend)
    }

    @Test
    fun getNormaString() {

        assertEquals("4,0 / 140,8", m1.normaString)
    }

    @Test
    fun getWeekendString() {
        assertEquals("1 / 3", m1.weekendString)
    }

    @Test
    fun getWorkDayString() {
        assertEquals("3 / 24", m1.workdayString)
    }
}