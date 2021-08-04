package ru.clementl.metrotimex.model.states

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.RepetitionInfo
import ru.clementl.metrotimex.converters.toInt
import ru.clementl.metrotimex.converters.toLong
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.Shift
import ru.clementl.metrotimex.model.data.WorkDayType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

internal class SimpleStateKtTest {

    val date01 = LocalDate.of(2021, 7, 1)
    val date02 = LocalDate.of(2021, 7, 2)
    val date03 = LocalDate.of(2021, 7, 3)
    val date04 = LocalDate.of(2021, 7, 4)
    val date05 = LocalDate.of(2021, 7, 5)
    val date06 = LocalDate.of(2021, 7, 6)
    val date07 = LocalDate.of(2021, 7, 7)
    val date08 = LocalDate.of(2021, 7, 8)
    val date09 = LocalDate.of(2021, 7, 9)

    val time_2_30 = LocalTime.of(2, 30)
    val time_3_30 = LocalTime.of(3, 30)
    val time_8_00 = LocalTime.of(8, 0)
    val time_8_30 = LocalTime.of(8, 30)
    val time_16_25 = LocalTime.of(16, 25)
    val time_4_30 = LocalTime.of(4, 30)
    val time_12_58 = LocalTime.of(12, 58)
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

    val day1_8_16 = DayStatus(date01.toLong(), WorkDayType.SHIFT.toInt(), shift_8_00_16_25)
    val day2_till_2_30 = DayStatus(date02.toLong(), WorkDayType.SHIFT.toInt(), shift_22_13_2_30_odd)
    val day3_weekend = DayStatus(date03.toLong(), WorkDayType.WEEKEND.toInt(), null)
    val day4_from_1_30 = DayStatus(date04.toLong(), WorkDayType.SHIFT.toInt(), shift_1_30_8_30)
    val day5_medic = DayStatus(date05.toLong(), WorkDayType.MEDIC_DAY.toInt(), null)
    val day6_out_night = DayStatus(date06.toLong(), WorkDayType.SHIFT.toInt(), shift_4_30_8_00)
    val day7_vacation = DayStatus(date07.toLong(), WorkDayType.VACATION_DAY.toInt(), null)
    val day8_till_2_30 = DayStatus(date08.toLong(), WorkDayType.SHIFT.toInt(), shift_22_13_2_30_odd)
    val day9_out_night = DayStatus(date09.toLong(), WorkDayType.SHIFT.toInt(), shift_4_30_8_00)

    val days = listOf(
        day1_8_16, day2_till_2_30, day3_weekend, day4_from_1_30, day5_medic, day6_out_night, day7_vacation, day8_till_2_30, day9_out_night
    )

    @BeforeEach
    fun setUp() {
    }

    @RepeatedTest(34)
    fun longSimpleState(repetitionInfo: RepetitionInfo) {
        val solution = listOf(
            LocalDateTime.of(2021, 7, 1, 7, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021,7,1,12,0).toLong() to ShiftSimpleState,
            LocalDateTime.of(2021, 7, 1, 19, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 2, 4, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 2, 19, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 2, 22, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 2, 23, 0).toLong() to ShiftSimpleState,
            LocalDateTime.of(2021, 7, 3, 0, 1).toLong() to ShiftSimpleState,
            LocalDateTime.of(2021, 7, 3, 2, 30).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 3, 2, 45).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 3, 2, 59).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 3, 3, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 3, 7, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 3, 12, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 3, 19, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 3, 23, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 4, 0, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 4, 1, 30).toLong() to ShiftSimpleState,
            LocalDateTime.of(2021, 7, 4, 2, 59).toLong() to ShiftSimpleState,
            LocalDateTime.of(2021, 7, 4, 3, 0).toLong() to ShiftSimpleState,
            LocalDateTime.of(2021, 7, 4, 6, 0).toLong() to ShiftSimpleState,
            LocalDateTime.of(2021, 7, 4, 10, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 4, 19, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 5, 2, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 5, 3, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 5, 16, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 6, 1, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 6, 3, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 6, 4, 30).toLong() to ShiftSimpleState,
            LocalDateTime.of(2021, 7, 6, 8, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 6, 22, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 7, 1, 0).toLong() to GapSimpleState,
            LocalDateTime.of(2021, 7, 7, 3, 0).toLong() to VacationSimpleState,
            LocalDateTime.of(2021, 7, 9, 3, 0).toLong() to NightGapSimpleState
        )
        for (i in 0 until repetitionInfo.totalRepetitions) {
            if (repetitionInfo.currentRepetition == i) {
                val (moment, expected) = solution[i]
                assertEquals(expected, moment.simpleState(days))
            }
        }

    }

    @Test
    fun tempTest() {
        val days = listOf(
            DayStatus(dateLong=1627592400000, workDayTypeInt=2, shift=null),
            DayStatus(dateLong=1627678800000, workDayTypeInt=2, shift=null)
        )
        val exp = SickSimpleState
        val actual = 1627735666390L.simpleState(days)
        assertEquals(exp, actual)
    }
}