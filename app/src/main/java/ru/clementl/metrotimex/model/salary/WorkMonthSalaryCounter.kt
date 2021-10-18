package ru.clementl.metrotimex.model.salary

import ru.clementl.metrotimex.*
import ru.clementl.metrotimex.converters.toLong
import ru.clementl.metrotimex.model.data.MachinistStatus
import ru.clementl.metrotimex.model.data.getClassQ
import ru.clementl.metrotimex.model.data.getMachinistStatus
import ru.clementl.metrotimex.model.data.getStageQ
import ru.clementl.metrotimex.model.norma.WorkMonth
import ru.clementl.metrotimex.utils.inFloatHours
import ru.clementl.metrotimex.utils.salaryStyle
import java.time.LocalDateTime
import java.util.*

class WorkMonthSalaryCounter(val workMonth: WorkMonth) : SalaryCounter {

    private val rate = workMonth.endStatus.ratePerMilli

    val base: Double
        get() = workMonth.workedInMillis() * rate
    val baseLineIncome: Double
        get() = workMonth.baseLineTimeMillis * rate
    val baseReserveIncome: Double
        get() = workMonth.baseReserveTimeMillis * workMonth.endStatus.rateReserveLightMilli
    val baseGapIncome: Double
        get() = workMonth.baseGapTimeMillis * rate * GAP_Q
    val eveningBonus: Double
        get() = workMonth.eveningMillis * EVENING_HOURS_Q * rate
    val nightBonus: Double
        get() = workMonth.nightMillis * NIGHT_HOURS_Q * rate
    val classBonus: Double
        get() = base * workMonth.endStatus.machinist.getClassQ()
    val masterBonus: Double
        get() = workMonth.asMasterMillis * rate * MASTER_Q
    val mentorBonus: Double
        get() = workMonth.asMentorMillis * rate * MENTOR_Q
    val stageBonus: Double
        get() = base * workMonth.endStatus.machinist.getStageQ(workMonth.endMilli!!)
    val premia: Double
        get() = workMonth.endStatus.machinist.monthBonus *
                (baseLineIncome +
                        baseReserveIncome +
                        baseGapIncome +
                        eveningBonus +
                        nightBonus +
                        classBonus +
                        masterBonus +
                        mentorBonus)

    val techUch: Double
        get() = if (workMonth.wasTechUch) TECH_UCH_Q * rate * 2 * HOUR_MILLI else 0.0

    val totalIncome: Double
        get() = baseLineIncome + baseReserveIncome + baseGapIncome + eveningBonus + nightBonus +
                classBonus + masterBonus + mentorBonus + premia + stageBonus + techUch

    val ndflSub: Double
        get() = totalIncome * NDFL

    val profsouzSub: Double
        get() = totalIncome * UNION_Q



    override fun getSalary(moment: Long): Double {
        return totalIncome - ndflSub - profsouzSub
    }

    // Квиток
    fun logList(): String {
        with(workMonth) {
            val status = endMilli.getMachinistStatus(statusChangeList)
            return """
            ----
            Месяц: ${workMonth.yearMonth.toString().toUpperCase(Locale.ROOT)}
            Повр. опл. по тариф. став | ${baseLineTimeMillis.inFloatHours()} | ${baseLineIncome.salaryStyle()}
            Опл. РЕЗЕРВ на поверхност | ${baseReserveTimeMillis.inFloatHours()} | ${baseReserveIncome.salaryStyle()}
            Разрывные часы            | ${baseGapTimeMillis.inFloatHours()} | ${baseGapIncome.salaryStyle()}
            ЕжемПремЛокБр             | ${status.machinist.monthBonus.times(100)}% | ${premia.salaryStyle()}
            Доплата за вечернее время | ${eveningMillis.inFloatHours()} | ${eveningBonus.salaryStyle()}
            Доплата за ночное время   | ${nightMillis.inFloatHours()} | ${nightBonus.salaryStyle()}
            Надбавка за класс квалиф  | ${endMilli?.getClassQ(statusChangeList)?.times(100)}% | ${classBonus.salaryStyle()}
            Техническая учеба         | 2,0ч | ${techUch.salaryStyle()}
            Допл.за практ.обуч.маш.   | ${asMentorMillis.inFloatHours()} | ${mentorBonus.salaryStyle()}
            Допл.за рук-во лок. бр.   | ${asMasterMillis.inFloatHours()} | ${masterBonus.salaryStyle()}
            Выслуга лет               | ${endMilli?.getStageQ(statusChangeList)?.times(100)}% | ${stageBonus.salaryStyle()}
            НДФЛ 13%                  |      | -${ndflSub.salaryStyle()}
            Профсоюзный взнос         |      | -${profsouzSub.salaryStyle()}
            
            
            Итого начислено:          |      | ${totalIncome.salaryStyle()}
            
            К выдаче на руки:         |      | ${getSalary(LocalDateTime.now().toLong()).salaryStyle()}
            
            
            
            
        """.trimIndent()
        }
    }
}