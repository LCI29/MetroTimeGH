package ru.clementl.metrotimex.model.salary

import ru.clementl.metrotimex.*
import ru.clementl.metrotimex.converters.toLong
import ru.clementl.metrotimex.model.data.*
import java.time.LocalDateTime

data class MachinistSalaryCounter(val machinistStatus: MachinistStatus, val day: DayStatus) : SalaryCounter {

    var moment: Long = 0L

    val goneSpan: TimeSpan
        get() = TimeSpan(day.startPoint.milli, moment)

    val goneDuration: Long
        get() = goneSpan.duration ?: 0L


    val shift = day.shift ?: throw Exception("No shift in day to count a salary")
    val eveningFrom = LocalDateTime.of(day.date, EVENING_FROM).toLong()
    val eveningTill = LocalDateTime.of(day.date, EVENING_TILL).toLong()
    val nightFrom1 = LocalDateTime.of(day.date.minusDays(1), NIGHT_FROM).toLong()
    val nightFrom2 = LocalDateTime.of(day.date, NIGHT_FROM).toLong()
    val nightTill1 = LocalDateTime.of(day.date, NIGHT_TILL).toLong()
    val nightTill2 = LocalDateTime.of(day.date.plusDays(1), NIGHT_TILL).toLong()
    val evening = TimeSpan(eveningFrom, eveningTill)
    val night1 = TimeSpan(nightFrom1, nightTill1)
    val night2 = TimeSpan(nightFrom2, nightTill2)

    val eveningDurationMilli: Long
        get() = goneSpan.intersect(evening)?.duration ?: 0
    val nightDurationMilli: Long
        get() = (goneSpan.intersect(night1)?.duration ?: 0) + (goneSpan.intersect(night2)?.duration ?: 0)

    val rate: Double = when {
        shift.isReserve == true -> machinistStatus.rateReserveLightMilli
        shift.hasAtz == true -> machinistStatus.rateAtzShiftPerMilli
        else -> machinistStatus.ratePerMilli
    }

    val baseLine: Double
        get() = goneDuration * machinistStatus.ratePerMilli

    val baseReserve: Double
        get() = goneDuration * machinistStatus.rateReserveLightMilli

    val baseATZ: Double
        get() = goneDuration * machinistStatus.rateAtzShiftPerMilli

    val baseSalary: Double
        get() {
            return when {
                shift.isReserve == true -> baseReserve
                shift.hasAtz == true -> baseATZ
                else -> baseLine
            }
        }

    val qualificationClassBonus: Double
        get() = machinistStatus.machinist.getClassQ() * baseLine

    val mentorBonus: Double
        get() = machinistStatus.machinist.getMentorQ() * baseLine

    val gapHours: Double
        get() = ADDING_GAP * GAP_Q * baseSalary

    val masterBonus: Double
        get() = machinistStatus.machinist.getMasterQ() * baseLine

    val eveningBonus: Double
        get() = rate * eveningDurationMilli * EVENING_HOURS_Q

    val nightBonus: Double
        get() = rate * nightDurationMilli * NIGHT_HOURS_Q

    val timeBonus: Double
        get() = eveningBonus + nightBonus

    val premiumBase: Double
        get() = baseSalary + gapHours + masterBonus + timeBonus + qualificationClassBonus + mentorBonus

    val premiumBonus: Double
        get() = machinistStatus.machinist.monthBonus * premiumBase

    val stageBonus: Double
        get() = machinistStatus.machinist.getStageQ(moment) * baseLine

    val income: Double
        get() {
            return (baseSalary +
                    qualificationClassBonus +
                    timeBonus +
                    stageBonus +
                    mentorBonus +
                    masterBonus +
                    premiumBonus +
                    gapHours).coerceAtLeast(0.0)
        }

    val ndflSubtraction: Double
        get() = NDFL * income

    val unionSubtraction: Double
        get() = UNION_Q * income

    override fun getSalary(moment: Long): Double {
        this.moment = moment.coerceAtMost(day.endPoint.milli)
        if (day.workDayType != WorkDayType.SHIFT) return 0.0
//        println(
//            """
//            baseSalary = $baseSalary
//
//            baseLine = $baseLine
//            baseReserve = $baseReserve
//            baseAtz = $baseATZ
//
//            duration = ${goneDuration / 1000}
//
//            eveningBonus = $eveningBonus
//            nightBonus = $nightBonus
//            qualificationClassBonus = $qualificationClassBonus
//            mentorBonus = $mentorBonus
//            masterBonus = $masterBonus
//            gapHours = $gapHours
//            premiumBonus = $premiumBonus
//            stageBonus = $stageBonus
//
//            INCOME = $income
//
//            NDFL = -$ndflSubtraction
//            PROFSOUZ = -$unionSubtraction
//        """.trimIndent()
//        )
        return income - ndflSubtraction - unionSubtraction

    }

    fun getFinalSalary(): Double {
        return getSalary(day.endPoint.milli)
    }
}

//fun main() {
//    val machinist = Machinist(
//        onPostSince = LocalDate.of(2013, 4, 16),
//        qualificationClass = 2,
//        isMaster = false,
//        isMentor = false,
//        monthBonus = 0.25,
//        isInUnion = true
//    )
//
//    val shift = Shift(
//        startTimeInt = LocalTime.of(8,56).toInt(),
//        endTimeInt = LocalTime.of(15,56).toInt(),
//        weekDayTypeString = "Р",
//        oddEven = 0,
//        isReserveInt = 0,
//        hasAtzInt = 0
//    )
//
//    val date = LocalDate.of(2021, 8, 14)
//
//    val day = DayStatus(date.toLong(), WorkDayType.SHIFT.toInt(), shift)
//
//    val now = LocalDateTime.of(date.plusDays(0), LocalTime.of(9, 56))
//
//    val counter = MachinistSalaryCounter(machinist, day)
//
//    // 1628890943292
//
//    // time = 1628891582972, salary = 344.95007754833887
//    println(now.toLong())
//
//    println(counter.getSalary(now.toLong()))
//}


