package ru.clementl.metrotimex.model.data

import java.time.LocalDate

class Machinist(
    val onPostSince: LocalDate = LocalDate.now().minusDays(1),
    val qualificationClass: Int = 4,
    val isMaster: Boolean = false,
    val isMentor: Boolean = false,
    val monthBonus: Double = 1.25
)