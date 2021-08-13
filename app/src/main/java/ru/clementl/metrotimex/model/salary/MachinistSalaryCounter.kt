package ru.clementl.metrotimex.model.salary

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import ru.clementl.metrotimex.*
import ru.clementl.metrotimex.converters.toInt
import ru.clementl.metrotimex.converters.toLong
import ru.clementl.metrotimex.model.data.*
import ru.clementl.metrotimex.model.states.TimePoint
import ru.clementl.metrotimex.utils.logd
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class MachinistSalaryCounter(val machinist: Machinist, val day: DayStatus) : SalaryCounter {

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
        shift.isReserve == true -> RATE_RESERVE_LIGHT_MILLI
        shift.hasAtz == true -> RATE_ATZ_SHIFT_PER_MILLI
        else -> RATE_PER_MILLI
    }

    val baseLine: Double
        get() = goneDuration * RATE_PER_MILLI

    val baseReserve: Double
        get() = goneDuration * RATE_RESERVE_LIGHT_MILLI

    val baseATZ: Double
        get() = goneDuration * RATE_ATZ_SHIFT_PER_MILLI

    val baseSalary: Double
        get() {
            return when {
                shift.isReserve == true -> baseReserve
                shift.hasAtz == true -> baseATZ
                else -> baseLine
            }
        }

    val qualificationClassBonus: Double
        get() = machinist.getClassQ() * baseLine

    val mentorBonus: Double
        get() = machinist.getMentorQ() * baseLine

    val gapHours: Double
        get() = ADDING_GAP * baseSalary

    val masterBonus: Double
        get() = machinist.getMasterQ() * baseLine

    val eveningBonus: Double
        get() = rate * eveningDurationMilli * EVENING_HOURS_Q

    val nightBonus: Double
        get() = rate * nightDurationMilli * NIGHT_HOURS_Q

    val timeBonus: Double
        get() = eveningBonus + nightBonus

    val premiumBase: Double
        get() = baseSalary + gapHours + masterBonus + timeBonus + qualificationClassBonus + mentorBonus

    val premiumBonus: Double
        get() = machinist.monthBonus * premiumBase

    val stageBonus: Double
        get() = machinist.getStageQ(moment) * baseLine

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
//        logd(
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
}

//fun main() {
//    val machinist = Machinist(
//        onPostSince = LocalDate.of(2013, 4, 16),
//        qualificationClass = 1,
//        isMaster = true,
//        isMentor = true,
//        monthBonus = 0.25,
//        isInUnion = true
//    )
//
//    val shift = Shift(
//        startTimeInt = LocalTime.of(0,52).toInt(),
//        endTimeInt = LocalTime.of(13,58).toInt(),
//        weekDayTypeString = "Р",
//        oddEven = 0,
//        isReserveInt = 0,
//        hasAtzInt = 1
//    )
//
//    val date = LocalDate.of(2021, 8, 14)
//
//    val day = DayStatus(date.toLong(), WorkDayType.SHIFT.toInt(), shift)
//
//    val now = LocalDateTime.of(date.plusDays(0), LocalTime.of(1, 3))
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


