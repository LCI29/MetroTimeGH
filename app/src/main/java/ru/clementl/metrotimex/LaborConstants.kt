package ru.clementl.metrotimex

import ru.clementl.metrotimex.model.norma.WorkMonth
import java.time.LocalDate
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
    YearMonth.of(2021, 12) to 158.4
)

val HOLIDAYS_2021 = setOf(
    LocalDate.of(2021, 1,1),
    LocalDate.of(2021, 1,2),
    LocalDate.of(2021, 1,3),
    LocalDate.of(2021, 1,4),
    LocalDate.of(2021, 1,5),
    LocalDate.of(2021, 1,6),
    LocalDate.of(2021, 1,7),
    LocalDate.of(2021, 1,8),
    LocalDate.of(2021, 1,9),
    LocalDate.of(2021, 1,10),
    LocalDate.of(2021, 2,23),
    LocalDate.of(2021, 3,8),
    LocalDate.of(2021, 5,1),
    LocalDate.of(2021, 5,2),
    LocalDate.of(2021, 5,3),
    LocalDate.of(2021, 5, 10),
    LocalDate.of(2021, 6, 12),
    LocalDate.of(2021, 6, 14),
    LocalDate.of(2021, 11, 4),
    LocalDate.of(2021, 11, 5),
    LocalDate.of(2021, 12, 31),
)
