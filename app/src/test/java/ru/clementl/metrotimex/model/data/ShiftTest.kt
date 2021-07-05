package ru.clementl.metrotimex.model.data

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.RepetitionInfo
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

const val n = 2 // repetition number. Change, if add examples

internal class ShiftTest {

    lateinit var exampleShifts: List<Shift>

    // 22.06.2021 85.2  8:36 СК - 16:25 БП
    val start0 = LocalDateTime.of(2021, 6, 22, 8, 36)
    val end0 = LocalDateTime.of(2021, 6, 22, 16, 25)

    // 30.12.1998 42.н  18:16 НАГ - 0:20 Д
    val start1 = LocalDateTime.of(1998, 12, 30, 18, 16)
    val end1 = LocalDateTime.of(1998, 12, 31, 0, 20)

    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        val shift1 = Shift(
            start0.toLocalDate(),
            start0.toLocalTime(),
            end0.toLocalTime(),
            "85.2", "СК", "БП"
        )

        val shift2 = Shift(
            start1.toLocalDate(),
            start1.toLocalTime(),
            end1.toLocalTime(),
            "42.н", "НАГ", "Д"
        )

        exampleShifts = listOf(shift1, shift2)

    }


    @org.junit.jupiter.api.RepeatedTest(n)
    fun getDate(repetitionInfo: RepetitionInfo) {
        val expected = listOf(
            start0.toLocalDate(),
            start1.toLocalDate()
        )
        for (i in 0 until repetitionInfo.totalRepetitions) {
            if (repetitionInfo.currentRepetition == i) {
                val actual = exampleShifts[i].date
                Assertions.assertEquals(expected[i], actual)
            }
        }


    }

    @org.junit.jupiter.api.RepeatedTest(n)
    fun getEndDate(repetitionInfo: RepetitionInfo) {
        val expected = listOf(
            end0.toLocalDate(),
            end1.toLocalDate()
        )
        for (i in 0 until repetitionInfo.totalRepetitions) {
            if (repetitionInfo.currentRepetition == i) {
                val actual = exampleShifts[i].endDate
                assertEquals(expected[i], actual)
            }
        }
    }

    @org.junit.jupiter.api.RepeatedTest(n)
    fun getType(repetitionInfo: RepetitionInfo) {
        val expected = listOf(TYPE_SHIFT, TYPE_SHIFT)
        for (i in 0 until repetitionInfo.totalRepetitions)
            assertEquals(expected[i], exampleShifts[i].type)
    }

    @org.junit.jupiter.api.RepeatedTest(n)
    fun getStartDateTime(repetitionInfo: RepetitionInfo) {
        val expected = listOf(
            start0,
            start1
        )
        for (i in 0 until repetitionInfo.totalRepetitions)
            assertEquals(expected[i], exampleShifts[i].startDateTime)
    }

    @org.junit.jupiter.api.RepeatedTest(n)
    fun getEndDateTime(repetitionInfo: RepetitionInfo) {
        val expected = listOf(
            end0,
            end1
        )
        for (i in 0 until repetitionInfo.totalRepetitions)
            assertEquals(expected[i], exampleShifts[i].endDateTime)
    }

    @org.junit.jupiter.api.RepeatedTest(n)
    fun getDuration(repetitionInfo: RepetitionInfo) {
        val expected = listOf(
            Duration.between(start0, end0),
            Duration.between(start1, end1)
        )
        for (i in 0 until repetitionInfo.totalRepetitions)
            assertEquals(expected[i], exampleShifts[i].duration)
    }

    @org.junit.jupiter.api.RepeatedTest(n)
    fun getStartTime(repetitionInfo: RepetitionInfo) {
        val expected = listOf(
            start0.toLocalTime(),
            start1.toLocalTime()
        )
        for (i in 0 until repetitionInfo.totalRepetitions)
            assertEquals(expected[i], exampleShifts[i].startTime)
    }

    @org.junit.jupiter.api.RepeatedTest(n)
    fun getEndTime(repetitionInfo: RepetitionInfo) {
        val expected = listOf(
            end0.toLocalTime(),
            end1.toLocalTime()
        )
        for (i in 0 until repetitionInfo.totalRepetitions)
            assertEquals(expected[i], exampleShifts[i].endTime)
    }

    @org.junit.jupiter.api.RepeatedTest(n)
    fun getShiftName(repetitionInfo: RepetitionInfo) {
        val expected = listOf(
            "85.2", "42.н"
        )
        for (i in 0 until repetitionInfo.totalRepetitions)
            assertTrue(expected[i] == exampleShifts[i].shiftName)
    }

    @org.junit.jupiter.api.RepeatedTest(n)
    fun getStartPlace(repetitionInfo: RepetitionInfo) {
        val expected = listOf("СК", "НАГ")
        for (i in 0 until repetitionInfo.totalRepetitions)
            assertTrue(expected[i] == exampleShifts[i].startPlace)
    }

    @org.junit.jupiter.api.RepeatedTest(n)
    fun getEndPlace(repetitionInfo: RepetitionInfo) {
        val expected = listOf("БП", "Д")
        for (i in 0 until repetitionInfo.totalRepetitions)
            assertTrue(expected[i] == exampleShifts[i].endPlace)
    }

}