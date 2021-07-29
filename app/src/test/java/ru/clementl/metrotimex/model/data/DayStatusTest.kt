package ru.clementl.metrotimex.model.data

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.RepetitionInfo
import ru.clementl.metrotimex.DAY_END_TIME
import ru.clementl.metrotimex.DAY_START_TIME
import ru.clementl.metrotimex.converters.toInt
import ru.clementl.metrotimex.converters.toLong
import ru.clementl.metrotimex.model.states.TimePoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

internal class DayStatusTest {

    val date_26_07_21 = LocalDate.of(2021, 7, 26)
    val date_31_12_20 = LocalDate.of(2020, 12, 31)
    val date_01_09_19 = LocalDate.of(2019, 9, 1)

    val time_8_00 = LocalTime.of(8, 0)
    val time_16_25 = LocalTime.of(16, 25)
    val time_4_30 = LocalTime.of(4, 30)
    val time_12_58 = LocalTime.of(12, 58)
    val time_16_19 = LocalTime.of(16, 19)
    val time_18_05 = LocalTime.of(18, 5)
    val time_23_54 = LocalTime.of(23, 54)
    val time_1_59 = LocalTime.of(1, 59)

    val name_952 = "95.2"
    val name_M18 = "М18"
    val name_ = ""
    val name_733p = "73.3+"

    val shift_8_00_16_25 = Shift(
        name_952, "Р", 0, time_8_00.toInt(), "ск", time_16_25.toInt(), "ск", 0, 0
    )
    val shift_18_05_1_59_even = Shift(
        name_733p, "В", 2, time_18_05.toInt(), "тим", time_1_59.toInt(), "ал", 0, 0
    )



    val day26_07_21_shift = DayStatus(date_26_07_21.toLong(), WorkDayType.SHIFT.toInt(), shift_8_00_16_25)
    val day1_09_21_shift = DayStatus(date_01_09_19.toLong(), WorkDayType.SHIFT.toInt(), shift_18_05_1_59_even)
    val day31_12_20_weekend = DayStatus(date_31_12_20.toLong(), WorkDayType.WEEKEND.toInt(), null)
    val day_error_26_07_21_shift = DayStatus(date_26_07_21.toLong(), WorkDayType.SHIFT.toInt(), shift_8_00_16_25)
    val medic_day_1_09_21 = DayStatus(date_01_09_19.toLong(), WorkDayType.MEDIC_DAY.toInt(), null)

    val days = listOf(
        day26_07_21_shift, day1_09_21_shift, day31_12_20_weekend, day_error_26_07_21_shift, medic_day_1_09_21
    )




    @BeforeEach
    fun setUp() {
    }

    @Test
    fun getWorkDayType() {
    }

    @Test
    fun getStartDateTime() {
    }

    @Test
    fun getEndDateTime() {
    }

    @RepeatedTest(5)
    fun getStartPoint(repetitionInfo: RepetitionInfo) {
        val expected = listOf(
            TimePoint(LocalDateTime.of(date_26_07_21, time_8_00).toLong(), WorkDayType.SHIFT.startPointCode),
            TimePoint(LocalDateTime.of(date_01_09_19, time_18_05).toLong(), WorkDayType.SHIFT.startPointCode),
            TimePoint(LocalDateTime.of(date_31_12_20, DAY_START_TIME).toLong(), WorkDayType.WEEKEND.startPointCode),
            TimePoint(LocalDateTime.of(date_26_07_21, time_8_00).toLong(), WorkDayType.SHIFT.startPointCode),
            TimePoint(LocalDateTime.of(date_01_09_19, DAY_START_TIME).toLong(), WorkDayType.MEDIC_DAY.startPointCode)
        )
        for (i in 0 until repetitionInfo.totalRepetitions) {
            if (repetitionInfo.currentRepetition == i) {
                println(i)
                println(repetitionInfo.currentRepetition)
                println(days[i].startPoint)
                assertEquals(expected[i], days[i].startPoint)
            }
        }
    }

    @RepeatedTest(5)
    fun getEndPoint(repetitionInfo: RepetitionInfo) {
        val expected = listOf(
            TimePoint(LocalDateTime.of(date_26_07_21, time_16_25).toLong() - 1, WorkDayType.SHIFT.endPointCode),
            TimePoint(LocalDateTime.of(date_01_09_19.plusDays(1), time_1_59).toLong() - 1, WorkDayType.SHIFT.endPointCode),
            TimePoint(LocalDateTime.of(date_31_12_20.plusDays(1), DAY_END_TIME).toLong(), WorkDayType.WEEKEND.endPointCode),
            TimePoint(LocalDateTime.of(date_26_07_21, time_16_25).toLong() - 1, WorkDayType.SHIFT.endPointCode),
            TimePoint(LocalDateTime.of(date_01_09_19.plusDays(1), DAY_END_TIME).toLong(), WorkDayType.MEDIC_DAY.endPointCode)
        )
        for (i in 0 until repetitionInfo.totalRepetitions) {
            if (repetitionInfo.currentRepetition == i) {
                println(i)
                println(repetitionInfo.currentRepetition)
                println(days[i].endPoint)
                assertEquals(expected[i], days[i].endPoint)
            }
        }
    }
}