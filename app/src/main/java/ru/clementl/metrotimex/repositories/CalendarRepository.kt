package ru.clementl.metrotimex.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.room.daos.CalendarDao

class CalendarRepository(private val calendarDao: CalendarDao) {

    val allDays: Flow<List<DayStatus>> = calendarDao.getAll()

    // Получение дня по дате
    fun getDayByDate(dateLong: Long): LiveData<DayStatus> {
        return calendarDao.getDayByDate(dateLong)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(dayStatus: DayStatus) {
        calendarDao.insert(dayStatus)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getLastDateLong(): Long? {
        return calendarDao.getLastDateLong()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(dayId: Long) {
        calendarDao.deleteDayById(dayId)
    }




}