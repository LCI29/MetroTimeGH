package ru.clementl.metrotimex.model.salary

import ru.clementl.metrotimex.model.data.DayStatus

interface SalaryCounter {
    fun getSalary(moment: Long): Double
}