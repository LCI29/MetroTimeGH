package ru.clementl.metrotimex.viewmodel

import androidx.lifecycle.ViewModel
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.Shift
import ru.clementl.metrotimex.model.data.WorkDayType
import ru.clementl.metrotimex.model.data.weekDayType
import ru.clementl.metrotimex.utils.logd
import ru.clementl.metrotimex.utils.oddEven
import java.time.LocalDate
import java.time.LocalTime

class SharedViewModel : ViewModel() {

    private val dates = listOf(
        LocalDate.of(2021, 7, 7),
        LocalDate.of(2021, 7, 8),
        LocalDate.of(2021, 7, 9),
        LocalDate.of(2021, 7, 9),
    )

    private val startTimes = listOf(
        LocalTime.of(8, 45),
        null,
        LocalTime.of(12, 30),
        LocalTime.of(17, 58)
    )

    private val endTimes = listOf(
        LocalTime.of(15, 54),
        null,
        LocalTime.of(18, 35),
        LocalTime.of(1, 59)
    )


    val dayList: List<DayStatus> = mutableListOf(
        DayStatus(
            dates[0], WorkDayType.SHIFT, Shift(
                name = "91.2",
                weekDayType = dates[0].weekDayType(),
                oddEven = dates[0].oddEven(endTimes[0]!!),
                startTime = startTimes[0]!!,
                startLoc = "СК",
                endTime = endTimes[0]!!,
                endLoc = "СК"
            )
        ),
        DayStatus(
            dates[1], WorkDayType.WEEKEND, null
        ),
        DayStatus(
            dates[2], WorkDayType.SHIFT, Shift(
                name = "PC.3",
                weekDayType = dates[2].weekDayType(),
                oddEven = dates[2].oddEven(endTimes[2]!!),
                startTime = startTimes[2]!!,
                startLoc = "СК",
                endTime = endTimes[2]!!,
                endLoc = "СК"
            )
        ),
        DayStatus(
            dates[3], WorkDayType.SHIFT, Shift(
                name = "87.н",
                weekDayType = dates[3].weekDayType(),
                oddEven = dates[3].oddEven(endTimes[3]!!),
                startTime = startTimes[3]!!,
                startLoc = "СК",
                endTime = endTimes[3]!!,
                endLoc = "СК"
        ))
    )

    override fun onCleared() {
        super.onCleared()
        logd("ShiftCreateViewModel cleared")
    }
}