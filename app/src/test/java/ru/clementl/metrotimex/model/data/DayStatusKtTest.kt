package ru.clementl.metrotimex.model.data

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.clementl.metrotimex.NIGHT_FROM
import ru.clementl.metrotimex.NIGHT_TILL
import ru.clementl.metrotimex.converters.toLong
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

internal class DayStatusKtTest {

    val day = DayStatus.workdayOf(LocalDate.of(2021, 7, 6), 5, 0, 7, 0)



    @Test
    fun nightSpans() {
        val expectedTimeSpans = listOf(
            TimeSpan(LocalDateTime.of(day.date, LocalTime.of(5, 0)).toLong(), LocalDateTime.of(day.date, NIGHT_TILL).toLong()),
            null,
            null
        )
        assertEquals(expectedTimeSpans, day.nightSpans())
    }
}