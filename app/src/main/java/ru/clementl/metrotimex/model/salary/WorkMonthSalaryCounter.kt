package ru.clementl.metrotimex.model.salary

import ru.clementl.metrotimex.*
import ru.clementl.metrotimex.converters.toLong
import ru.clementl.metrotimex.model.data.*
import ru.clementl.metrotimex.screens.norma.WorkMonth
import ru.clementl.metrotimex.utils.inFloatHours
import ru.clementl.metrotimex.utils.logd
import ru.clementl.metrotimex.utils.salaryStyle
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.*

class WorkMonthSalaryCounter(val workMonth: WorkMonth) : SalaryCounter {

    private val rate = workMonth.endStatus.ratePerMilli

    init {
        logd("WorkMonthSalaryCounter created")
    }

    val avgDaily: Double by lazy {
            val a = AverageDailyCounter().count(
            workMonth.yearMonth.atDay(1),
            workMonth.calendar,
            workMonth.statusChangeList,
            workMonth.yearMonthData)
            a
    }

    val base: Double
        get() = workMonth.workedInMillis() * rate + workMonth.holidayMillis * rate
    val normaBase: Double
        get() = workMonth.normaWorkedInMillis() * rate
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
        get() = normaBase * workMonth.endStatus.machinist.getStageQ(workMonth.endMilli!!)
    val premia: Double
        get() = workMonth.premia *
                (baseLineIncome +
                        baseReserveIncome +
                        baseGapIncome +
                        eveningBonus +
                        nightBonus +
                        classBonus +
                        masterBonus +
                        mentorBonus +
                        overWorkPay +
                        holidayPay)

    val overWorkPay: Double
        get() = workMonth.overworkMillis * rate

    val overWorkOverPayHalf: Double
        get() = workMonth.overworkMillisForHalfPay * rate * 0.5

    val overWorkOverPayFull: Double
        get() = workMonth.overworkMillisForFullPay * rate

    val holidayPay: Double
        get() = workMonth.holidayMillis * rate

    val holidayOverPay: Double
        get() = workMonth.holidayMillis * rate



    val techUch: Double
        get() = if (workMonth.wasTechUch) TECH_UCH_Q * rate * 2 * HOUR_MILLI else 0.0

    val sickListPay: Double
        get() {
            val counter = SickListCounter()
            return workMonth.sickListDays.sumOf { counter.count(
                it.date,
                it.dateLong.getMachinistStatus(workMonth.statusChangeList),
                it.workDayType
            ) }
        }

    val vacationPay: Double
        get() {
            if (workMonth.vacationDays.count() < 1) return 0.0
            val payedDays = workMonth.vacationDays.count { !it.isPublicHoliday() }
            return payedDays * avgDaily
        }

    // Medic days pay
    val medicPay: Double
        get() {
            if (workMonth.medicDays.count() < 1) return 0.0
            val payedDays = workMonth.medicDays.count()
            if (payedDays < 1) return 0.0
            workMonth.avgHoursPerDay?.let { hoursPerDay ->
                val payPerDay = avgDaily / hoursPerDay * HOURS_FOR_MEDIC_DAY
                return payedDays * payPerDay
            }
            return payedDays * avgDaily
        }

    // Donor days pay
    val donorPay: Double
        get() {
            if (workMonth.donorDays.count() < 1) return 0.0
            val payedDays = workMonth.donorDays.count { !it.isPublicHoliday() && it.date.dayOfWeek != DayOfWeek.SUNDAY }
            if (payedDays < 1) return 0.0
            workMonth.avgHoursPerDay?.let { hoursPerDay ->
                val payPerDay = avgDaily / hoursPerDay * HOURS_FOR_DONOR_DAY
                return payedDays * payPerDay
            }
            return payedDays * avgDaily
        }

    val incomeForDailyAverage: Double
        get() = baseLineIncome + baseReserveIncome + baseGapIncome + eveningBonus + nightBonus +
                classBonus + masterBonus + mentorBonus + premia + stageBonus + techUch +
                overWorkPay + overWorkOverPayHalf + overWorkOverPayFull + holidayPay +
                holidayOverPay

    val totalIncome: Double
        get() = baseLineIncome + baseReserveIncome + baseGapIncome + eveningBonus + nightBonus +
                classBonus + masterBonus + mentorBonus + premia + stageBonus + techUch +
                sickListPay + vacationPay + medicPay + donorPay + overWorkPay + overWorkOverPayHalf +
                overWorkOverPayFull + holidayPay + holidayOverPay

    val ndflSub: Double
        get() = totalIncome * NDFL

    val profsouzSub: Double
        get() = totalIncome * UNION_Q

    // For avg per hour
    val totalIncomeWithoutVacation: Double
        get() = baseLineIncome + baseReserveIncome + baseGapIncome + eveningBonus + nightBonus +
                classBonus + masterBonus + mentorBonus + premia + stageBonus + techUch +
                overWorkPay + overWorkOverPayHalf +
                overWorkOverPayFull + holidayPay + holidayOverPay

    val ndflSubWithoutVacation: Double
        get() = totalIncomeWithoutVacation * NDFL

    val profsouzSubWithoutVacation: Double
        get() = totalIncomeWithoutVacation * UNION_Q



    override fun getSalary(moment: Long): Double {
        return totalIncome - ndflSub - profsouzSub
    }

    fun getSalaryWithoutVacation(moment: Long): Double {
        return totalIncomeWithoutVacation - ndflSubWithoutVacation - profsouzSubWithoutVacation
    }



    // ????????????
    fun logList(): String {
        with(workMonth) {
            val status = endMilli.getMachinistStatus(statusChangeList)
            return """
            ----
            ??????????: ${workMonth.yearMonth.toString().toUpperCase(Locale.ROOT)}
            ????????. ??????. ???? ??????????. ???????? | ${baseLineTimeMillis.inFloatHours()} | ${baseLineIncome.salaryStyle()}
            ??????. ???????????? ???? ???????????????????? | ${baseReserveTimeMillis.inFloatHours()} | ${baseReserveIncome.salaryStyle()}
            ?????????????????? ????????            | ${baseGapTimeMillis.inFloatHours()} | ${baseGapIncome.salaryStyle()}
            ??????????????????????????             | ${status.machinist.monthBonus.times(100)}% | ${this@WorkMonthSalaryCounter.premia.salaryStyle()}
            ?????????????? ???? ???????????????? ?????????? | ${eveningMillis.inFloatHours()} | ${eveningBonus.salaryStyle()}
            ?????????????? ???? ???????????? ??????????   | ${nightMillis.inFloatHours()} | ${nightBonus.salaryStyle()}
            ??????????????????????????????.100%      | ${overworkMillis.inFloatHours()} | ${overWorkPay.salaryStyle()}
            ????????????????????????????????.50%      | ${overworkMillisForHalfPay.inFloatHours()} | ${overWorkOverPayHalf.salaryStyle()}
            ????????????????????????????????.100%     | ${overworkMillisForFullPay.inFloatHours()} | ${overWorkOverPayFull.salaryStyle()}
            ????????????????????????????????????????????????  | ${holidayMillis.inFloatHours()} | ${holidayPay.salaryStyle()}
            ?????????????????????????????????????????????????? | ${holidayMillis.inFloatHours()} | ${holidayOverPay.salaryStyle()}
            ???????????????? ???? ?????????? ????????????  | ${endMilli?.getClassQ(statusChangeList)?.times(100)}% | ${classBonus.salaryStyle()}
            ?????????????????????? ??????????         | 2,0?? | ${techUch.salaryStyle()}
            ????????.???? ??????????.????????.??????.   | ${asMentorMillis.inFloatHours()} | ${mentorBonus.salaryStyle()}
            ????????.???? ??????-???? ??????. ????.   | ${asMasterMillis.inFloatHours()} | ${masterBonus.salaryStyle()}
            ?????????????? ??????               | ${endMilli?.getStageQ(statusChangeList)?.times(100)}% | ${stageBonus.salaryStyle()}
            ???????????? ???? ??/??             | ${sickListDays.count()},0 ?? | ${sickListPay.salaryStyle()}
            ??????????????????                 | ${vacationDays.count()},0 ?? | ${vacationPay.salaryStyle()}
            ???????????? ???? ??????????????????????     | ${medicDays.count() * 6},0?? | ${medicPay.salaryStyle()}
            ???????????? ?????????????????? ??????      | ${donorDays.count() * 6},0?? | ${donorPay.salaryStyle()}
            ???????? 13%                  |      | -${ndflSub.salaryStyle()}
            ?????????????????????? ??????????         |      | -${profsouzSub.salaryStyle()}
            
            
            ?????????? ??????????????????:          |      | ${totalIncome.salaryStyle()}
            
            ?? ???????????? ???? ????????:         |      | ${getSalary(LocalDateTime.now().toLong()).salaryStyle()}
            
            ?????????????????????????? ??????????       |      | ${avgDaily.salaryStyle()}
            
            
            
            
        """.trimIndent()
        }
    }
}