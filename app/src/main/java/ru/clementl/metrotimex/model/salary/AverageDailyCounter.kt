package ru.clementl.metrotimex.model.salary

import kotlinx.coroutines.*
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.MachinistStatus
import ru.clementl.metrotimex.model.norma.WorkMonth
import ru.clementl.metrotimex.model.norma.YearMonthData
import ru.clementl.metrotimex.utils.logd
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.YearMonth

class AverageDailyCounter {

    // Returns average earned per day in 3 months before the date
     fun count(date: LocalDate, calendar: List<DayStatus>, statusChangeList: List<MachinistStatus>, yearMonthData: List<YearMonthData>): Double {
            val months = mutableSetOf<YearMonth>()
            for (i in 1..3) {
                months.add(YearMonth.from(date.minusMonths(i.toLong())))
            }
            val workMonths = mutableSetOf<WorkMonth>()
            months.forEach { workMonths.add(WorkMonth.of(it, calendar, statusChangeList, yearMonthData)) }

            val daysWorked = workMonths.sumOf { it.daysWorkedForAverageDaily.count() }
        logd("Days worked $daysWorked")
            val totalIncome = workMonths.sumOf {
                val wmCounter = WorkMonthSalaryCounter(it)
                wmCounter.incomeForDailyAverage
            }
//        logd("Total income $totalIncome")
            return if (daysWorked == 0) 0.0 else (totalIncome / daysWorked)

    }
}
