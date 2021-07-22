package ru.clementl.metrotimex.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import ru.clementl.metrotimex.converters.ShiftConverter
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.room.daos.CalendarDao
import java.time.LocalDate

class CalendarRepository(private val calendarDao: CalendarDao) {

    val allDays: Flow<List<DayStatus>> = calendarDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(dayStatus: DayStatus) {
        calendarDao.insert(dayStatus)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getLastDate(): Long {
        return calendarDao.getLastDate()
    }




}