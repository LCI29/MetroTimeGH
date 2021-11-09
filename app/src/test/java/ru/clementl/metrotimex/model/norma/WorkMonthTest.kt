package ru.clementl.metrotimex.model.norma

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import ru.clementl.metrotimex.ADDING_GAP
import ru.clementl.metrotimex.HOUR_MILLI
import ru.clementl.metrotimex.model.data.DayStatus.Companion.medicDayOf
import ru.clementl.metrotimex.model.data.DayStatus.Companion.sickListDayOf
import ru.clementl.metrotimex.model.data.DayStatus.Companion.weekendOf
import ru.clementl.metrotimex.model.data.DayStatus.Companion.workdayOf
import ru.clementl.metrotimex.model.data.Shift
import ru.clementl.metrotimex.model.data.WorkDayType.*
import java.time.LocalDate

internal class WorkMonthTest {



    val calendar = listOf(

        // 19.05 15:00 - 16:00
        workdayOf(LocalDate.of(2021, 5, 19), 15, 0, 16, 0),

        // 30.06 23:00 - 1:00, R
        workdayOf(LocalDate.of(2021, 6, 30), 23, 0, 1, 0, reserve = true),

        /**
         * July 2021
         *   Пн Вт Ср Чт Пт Сб Вс
         *             1  2  3  4
         *    5  6  7  8  9 10 11
         *   12 13 14 15 16 17 18
         *   19 20 21 22 23 24 25
         *   26 27 28 29 30 31
         */

        // 1.07 16:00 - 17:00
        workdayOf(LocalDate.of(2021, 7, 1), 16, 0, 17, 0),
        // 2.07 - 4.07 Sick list
        sickListDayOf(LocalDate.of(2021, 7, 2)),
        sickListDayOf(LocalDate.of(2021, 7, 3)),
        sickListDayOf(LocalDate.of(2021, 7, 4)),
        // 6.07 5:00 - 7:00
        workdayOf(LocalDate.of(2021, 7, 6), 5, 0, 7, 0),
        // 7.07 21:00 - 7:00
        workdayOf(LocalDate.of(2021, 7, 7), 21, 0, 7, 0),
        // 12.07 Medic day
        medicDayOf(LocalDate.of(2021, 7, 12)),
        // 20.07 ВЫХ
        weekendOf(LocalDate.of(2021, 7, 20)),
        // 27.07 16:00 - 17:00 R, ATZ
        workdayOf(LocalDate.of(2021, 7, 27), 16, 0, 17, 0, reserve = true, atz = true),
        // 31.07 23:00 - 1:00, ATZ
        workdayOf(LocalDate.of(2021, 7, 31), 23, 0, 1, 0, atz = true),




    )

    val m1 = WorkMonth.of(2021, 7, calendar, statusChangeList = listOf(), yearMonthData = listOf())
    val m1_totalHours = 16
    val m1_lineHours = 13
    val m1_reserveHours = 3
    val m1_eveningHours = 3
    val m1_nightHours = 11
    val m1_allShiftsCount = 5
    val m1_totalDaysCount = 10

    fun printStats() {
        println(m1.standardNormaHours)
        println(m1.standardNormaDays)
        println(m1.realNormaDays)
        println(m1.realNormaHours)
        println(m1.workedInHours)
    }


    @Test
    fun workedInMillis() {
        assertEquals(m1_totalHours * HOUR_MILLI - 6, m1.workedInMillis())
        assertEquals(m1_allShiftsCount, m1.allShifts.count())
        assertEquals(m1_totalDaysCount, m1.listOfDays.count())
    }

    @Test
    fun getSundaysCount() {
        assertEquals(4, m1.sundaysAndHolidaysCount)
    }

    @Test
    fun getStandardNormaDays() {
        assertEquals(27, m1.standardNormaDays)

        val m2 = WorkMonth.of(2021, 12, calendar, statusChangeList = listOf(), yearMonthData = listOf())
        assertEquals(26, m2.standardNormaDays)
    }

    @Test
    fun realNormaDays() {
        assertEquals(24, m1.realNormaDays)
    }

    @Test
    fun countOf() {
        assertEquals(5, m1.countOf(SHIFT))
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

        assertEquals("16,0 / 140,8", m1.normaString)
    }

    @Test
    fun getWeekendString() {
        assertEquals("1 / 3", m1.weekendString)
    }

    @Test
    fun getWorkDayString() {
        assertEquals("5 / 24", m1.workdayString)
    }

    @Test
    fun linePlusReserveToTotalWorkedEquality() {
        val line = m1.baseLineTimeMillis
        val reserve = m1.baseReserveTimeMillis
        val total = m1.workedInMillis()
        assertEquals(total, line + reserve)
    }

    @Test
    fun getBaseLineTimeMillis() {
        assertEquals(m1_lineHours * HOUR_MILLI - 3, m1.baseLineTimeMillis)
    }

    @Test
    fun getBaseReserveTimeMillis() {
        assertEquals(m1_reserveHours * HOUR_MILLI - m1_reserveHours, m1.baseReserveTimeMillis)
    }

    @Test
    fun getBaseGapTimeMillis() {
        assertEquals(((m1_totalHours * HOUR_MILLI) * ADDING_GAP - 3).toLong(), m1.baseGapTimeMillis)
    }

    @Test
    fun getEveningMillis() {
        assertEquals(m1_eveningHours * HOUR_MILLI - m1_eveningHours, m1.eveningMillis)
    }

    @Test
    fun getNightMillis() {
        assertEquals(m1_nightHours * HOUR_MILLI - 5, m1.nightMillis)
    }


}