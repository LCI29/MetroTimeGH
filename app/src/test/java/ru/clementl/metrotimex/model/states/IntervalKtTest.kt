package ru.clementl.metrotimex.model.states

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.RepetitionInfo
import ru.clementl.metrotimex.NIGHT_GAP_MAX_DURATION
import ru.clementl.metrotimex.SHIFT_EP
import ru.clementl.metrotimex.SHIFT_SP
import ru.clementl.metrotimex.converters.toInt
import ru.clementl.metrotimex.converters.toLong
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.Shift
import ru.clementl.metrotimex.model.data.WorkDayType.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

internal class IntervalKtTest {

    val date01 = LocalDate.of(2021, 7, 1)
    val date02 = LocalDate.of(2021, 7, 2)
    val date03 = LocalDate.of(2021, 7, 3)
    val date04 = LocalDate.of(2021, 7, 4)
    val date05 = LocalDate.of(2021, 7, 5)
    val date06 = LocalDate.of(2021, 7, 6)
    val date07 = LocalDate.of(2021, 7, 7)

    val time_2_30 = LocalTime.of(2, 30)
    val time_3_30 = LocalTime.of(3, 30)
    val time_8_00 = LocalTime.of(8, 0)
    val time_8_30 = LocalTime.of(8, 30)
    val time_16_25 = LocalTime.of(16, 25)
    val time_4_30 = LocalTime.of(4, 30)
    val time_12_00 = LocalTime.of(12, 0)
    val time_16_19 = LocalTime.of(16, 19)
    val time_18_05 = LocalTime.of(18, 5)
    val time_22_13 = LocalTime.of(22, 13)
    val time_23_54 = LocalTime.of(23, 54)
    val time_1_30 = LocalTime.of(1, 30)
    val time_1_59 = LocalTime.of(1, 59)

    val name_952 = "95.2"
    val name_M18 = "М18"
    val name_ = ""
    val name_733p = "73.3+"
    val name_late_evening_2_30 = "До 2:30"
    val name_late_evening_3_30 = "До 3:30"
    val name_new_year = "НГ П8"
    val name_out_night = "С ночи"

    val shift_8_00_16_25 = Shift(
        name_952, "Р", 0, time_8_00.toInt(), "ск", time_16_25.toInt(), "ск", 0, 0
    )
    val shift_18_05_1_59_even = Shift(
        name_733p, "В", 2, time_18_05.toInt(), "тим", time_1_59.toInt(), "ал", 0, 0
    )
    val shift_22_13_2_30_odd = Shift(
        name_late_evening_2_30, "Р", 1, time_22_13.toInt(), "Д", time_2_30.toInt(), "Д", 0, 0
    )
    val shift_23_54_3_30_even = Shift(
        name_late_evening_3_30, "Р", 2, time_23_54.toInt(), "Д", time_3_30.toInt(), "Д", 0, 0
    )
    val shift_1_30_8_30 = Shift(
        name_new_year, "В", 0, time_1_30.toInt(), "СК", time_8_30.toInt(), "СК", 0, 0
    )
    val shift_4_30_8_00 = Shift(
        name_out_night, "В", 0, time_4_30.toInt(), "", time_8_00.toInt(), "", 0, 0
    )

    val day1_8_16 = DayStatus(date01.toLong(), SHIFT.toInt(), shift_8_00_16_25)
    val day2_till_2_30 = DayStatus(date02.toLong(), SHIFT.toInt(), shift_22_13_2_30_odd)
    val day3_weekend = DayStatus(date03.toLong(), WEEKEND.toInt(), null)
    val day4_from_1_30 = DayStatus(date04.toLong(), SHIFT.toInt(), shift_1_30_8_30)
    val day5_medic = DayStatus(date05.toLong(), MEDIC_DAY.toInt(), null)
    val day6_out_night = DayStatus(date06.toLong(), SHIFT.toInt(), shift_4_30_8_00)
    val day7_vacation = DayStatus(date07.toLong(), VACATION_DAY.toInt(), null)

    val days = listOf(
        day1_8_16, day2_till_2_30, day3_weekend, day4_from_1_30, day5_medic, day6_out_night, day7_vacation
    )

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun getInterval() {
    }

    @RepeatedTest(6)
    fun getPointBeforeFrom(repetitionInfo: RepetitionInfo) {
        val t2021_07_06__8_00 = LocalDateTime.of(date06, time_8_00).toLong()
        val t2021_07_01__12_00 = LocalDateTime.of(date01, time_12_00).toLong()
        when(repetitionInfo.currentRepetition) {
            1 -> {
                val day = day2_till_2_30
                val exp = day2_till_2_30.endPoint
                val act = t2021_07_06__8_00.getPointBeforeFrom(day)
                assertEquals(exp, act)
            }
            2 -> {
                val day = day6_out_night
                val exp = day6_out_night.endPoint
                val act = t2021_07_06__8_00.getPointBeforeFrom(day)
                assertEquals(exp, act)
            }
            3 -> {
                val day = day7_vacation
                val act = t2021_07_06__8_00.getPointBeforeFrom(day)
                assertNull(act)
            }
            4 -> {
                val day = day1_8_16
                val exp = day1_8_16.startPoint
                val act = t2021_07_01__12_00.getPointBeforeFrom(day)
                assertEquals(exp, act)
            }
            5 -> {
                val day = day3_weekend
                val exp = day3_weekend.endPoint
                val act = t2021_07_06__8_00.getPointBeforeFrom(day)
                assertEquals(exp, act)
            }
            6 -> {
                val day = day6_out_night
                val exp = day6_out_night.endPoint
                val act = (t2021_07_06__8_00 - 1).getPointBeforeFrom(day)
                assertEquals(exp, act)
            }
        }


    }

    @RepeatedTest(5)
    fun getPointAfterFrom(repetitionInfo: RepetitionInfo) {
        val t2021_07_06__8_00 = LocalDateTime.of(date06, time_8_00).toLong()
        val t2021_07_01__12_00 = LocalDateTime.of(date01, time_12_00).toLong()
        when (repetitionInfo.currentRepetition) {
            1 -> {
                val day = day1_8_16
                val exp = day1_8_16.endPoint
                val act = t2021_07_01__12_00.getPointAfterFrom(day)
                assertEquals(exp, act)
            }
            2 -> {
                val day = day1_8_16
                val act = t2021_07_06__8_00.getPointAfterFrom(day)
                assertNull(act)
            }
            3 -> {
                val day = day3_weekend
                val exp = day3_weekend.startPoint
                val act = t2021_07_01__12_00.getPointAfterFrom(day)
                assertEquals(exp, act)
            }
            4 -> {
                val day = day3_weekend
                val act = t2021_07_06__8_00.getPointAfterFrom(day)
                assertNull(act)
            }
            5 -> {
                val day = day6_out_night
                val act = t2021_07_06__8_00.minus(1).getPointAfterFrom(day)
                assertNull(act)
            }
        }
    }

    @Test
    fun durationTest() {
        val nightGapStart = LocalDateTime.of(2021,7,21,23,0)
        val nightGapEnd = LocalDateTime.of(2021, 7, 22, 6, 0)
        val interval = Interval(TimePoint(nightGapStart.toLong(), SHIFT_EP), TimePoint(nightGapEnd.toLong(), SHIFT_SP))
        assertEquals(NIGHT_GAP_MAX_DURATION, interval.duration)
    }
}