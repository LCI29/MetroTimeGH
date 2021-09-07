package ru.clementl.metrotimex.model.salary

import ru.clementl.metrotimex.model.norma.WorkMonth

class WorkMonthSalaryCounter(val month: WorkMonth) : SalaryCounter {




    override fun getSalary(moment: Long): Double {

        return 0.0
    }
}