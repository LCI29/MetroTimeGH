package ru.clementl.metrotimex.utils

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.RepetitionInfo
import java.time.LocalDate
import java.time.LocalTime

internal class TimeUtilsKtTest {

    @RepeatedTest(3)
    fun oddEven(repetitionInfo: RepetitionInfo) {
        if (repetitionInfo.currentRepetition == 1) {
            val startDate = LocalDate.of(2021, 7, 13)
            val endTime = LocalTime.of(0, 15)
            val expected = 2
            val actual = startDate.oddEven(endTime)
            assertEquals(expected, actual)
        }
        if (repetitionInfo.currentRepetition == 2) {
            val startDate = LocalDate.of(2021, 7, 13)
            val endTime = LocalTime.of(16, 20)
            val expected = 0
            val actual = startDate.oddEven(endTime)
            assertEquals(expected, actual)
        }
        if (repetitionInfo.currentRepetition == 3) {
            val startDate = LocalDate.of(2021, 7, 31)
            val endTime = LocalTime.of(23, 54)
            val expected = 1
            val actual = startDate.oddEven(endTime)
            assertEquals(expected, actual)
        }
    }
}