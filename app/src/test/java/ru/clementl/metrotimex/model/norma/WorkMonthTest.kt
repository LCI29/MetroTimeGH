package ru.clementl.metrotimex.model.norma

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.clementl.metrotimex.model.data.DayStatus.Companion.medicDayOf
import ru.clementl.metrotimex.model.data.DayStatus.Companion.sickListDayOf
import ru.clementl.metrotimex.model.data.DayStatus.Companion.weekendOf
import ru.clementl.metrotimex.model.data.DayStatus.Companion.workdayOf
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
        medicDayOf(LocalDate.of(2021, 7, 12))
    )

    @Test
    fun workedInMillis() {
        val m1 = WorkMonth.of(2021, 7)
        assertEquals(3600 * 4 * 1000L - 4, m1.workedInMillis(calendar))
        assertEquals(3, m1.allShifts(calendar).count())
        assertEquals(7, m1.allDays(calendar).count())
    }

    @Test
    fun getSundaysCount() {
        val m1 = WorkMonth.of(2021, 7)
        assertEquals(4, m1.sundaysAndHolidaysCount)
    }

    @Test
    fun getStandardNormaDays() {
        val m1 = WorkMonth.of(2021, 7)
        assertEquals(27, m1.standardNormaDays)

        val m2 = WorkMonth.of(2021, 12)
        assertEquals(26, m2.standardNormaDays)
    }

    @Test
    fun realNormaDays() {
        val m1 = WorkMonth.of(2021, 7)
        assertEquals(24, m1.realNormaDays(calendar))
    }
}