package ru.clementl.metrotimex

import java.time.LocalDate
import java.time.Year
import java.time.YearMonth

val NORMA_MAP = mapOf(
    YearMonth.of(2021, 1) to 108.0,
    YearMonth.of(2021, 2) to 135.8,
    YearMonth.of(2021, 3) to 158.4,
    YearMonth.of(2021, 4) to 157.4,
    YearMonth.of(2021, 5) to 136.8,
    YearMonth.of(2021, 6) to 150.2,
    YearMonth.of(2021, 7) to 158.4,
    YearMonth.of(2021, 8) to 158.4,
    YearMonth.of(2021, 9) to 158.4,
    YearMonth.of(2021, 10) to 151.2,
    YearMonth.of(2021, 11) to 143.0,
    YearMonth.of(2021, 12) to 158.4,
    // 2022
    YearMonth.of(2022, 1) to 115.2,
    YearMonth.of(2022, 2) to 135.8,
    YearMonth.of(2022, 3) to 157.4,
    YearMonth.of(2022, 4) to 151.2,
    YearMonth.of(2022, 5) to 129.6,
    YearMonth.of(2022, 6) to 151.2,
    YearMonth.of(2022, 7) to 151.2,
    YearMonth.of(2022, 8) to 165.6,
    YearMonth.of(2022, 9) to 158.4,
    YearMonth.of(2022, 10) to 151.2,
    YearMonth.of(2022, 11) to 150.2,
    YearMonth.of(2022, 12) to 158.4
)

val HOLIDAYS = setOf(
    LocalDate.of(2021, 1,1),
    LocalDate.of(2021, 1,2),
    LocalDate.of(2021, 1,3),
    LocalDate.of(2021, 1,4),
    LocalDate.of(2021, 1,5),
    LocalDate.of(2021, 1,6),
    LocalDate.of(2021, 1,7),
    LocalDate.of(2021, 1,8),
    LocalDate.of(2021, 2,23),
    LocalDate.of(2021, 3,8),
    LocalDate.of(2021, 5,1),
    LocalDate.of(2021, 5, 9),
    LocalDate.of(2021, 6, 12),
    LocalDate.of(2021, 11, 4),
    LocalDate.of(2021, 12, 31),

    // 2022
    LocalDate.of(2022, 1,1),
    LocalDate.of(2022, 1,2),
    LocalDate.of(2022, 1,3),
    LocalDate.of(2022, 1,4),
    LocalDate.of(2022, 1,5),
    LocalDate.of(2022, 1,6),
    LocalDate.of(2022, 1,7),
    LocalDate.of(2022, 1,8),
    LocalDate.of(2022, 2,23),
    LocalDate.of(2022, 3,8),
    LocalDate.of(2022, 5,1),
    LocalDate.of(2022, 5, 9),
    LocalDate.of(2022, 6, 12),
    LocalDate.of(2022, 11, 4),
)

val CHANGED_WEEKENDS = setOf(
    LocalDate.of(2021, 2, 22),
    LocalDate.of(2021, 11, 5),
    LocalDate.of(2021, 12, 31),

    //2022

    LocalDate.of(2022, 5, 3),
    LocalDate.of(2022, 5, 10),
    LocalDate.of(2022, 3, 7),
)

val WORKING_WEEKENDS = setOf(
    LocalDate.of(2021, 2, 20),

    //2022
    LocalDate.of(2022, 3, 5)
)

val SHORTENED_DAYS = setOf(
    LocalDate.of(2021, 2, 20),
    LocalDate.of(2021, 4, 30),
    LocalDate.of(2021, 6, 11),
    LocalDate.of(2021, 11, 3),

    // 2022
    LocalDate.of(2022, 2, 22),
    LocalDate.of(2022, 3, 5),
    LocalDate.of(2022, 11, 3),
)

data class YearConstant(val year: Year, val mrot: Double, val sickDayMaxPay: Double)

val YEAR_CONSTS = setOf(
    YearConstant(Year.of(2019), 11280.00, 2150.68),
    YearConstant(Year.of(2020), 12130.00, 2301.37),
    YearConstant(Year.of(2021), 12792.00, 2434.24),
    YearConstant(Year.of(2022), 13617.00, 2572.60),
)

// SickListQ
const val SICK_Q_UNDER_6_MONTHS = 0.0
const val SICK_Q_UNDER_5_YEARS = 0.6
const val SICK_Q_UNDER_8_YEARS = 0.8
const val SICK_Q_MAX = 1.0

// Overwork
const val HOURS_PAYED_HALF: Int = 2
const val MILLIS_PAYED_HALF: Long = HOURS_PAYED_HALF * 60 * 60 * 1000L




