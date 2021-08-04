package ru.clementl.metrotimex.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.joinAll
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.room.daos.CalendarDao

class CalendarRepository(private val calendarDao: CalendarDao) {

    val allDays: Flow<List<DayStatus>> = calendarDao.getAll()

    // Получение дня по дате
    fun getLiveDayByDate(dateLong: Long): LiveData<DayStatus> {
        return calendarDao.getLiveDayByDate(dateLong)
    }

    @WorkerThread
    suspend fun getDaybyDate(dateLong: Long): DayStatus?{
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

    @WorkerThread
    suspend fun loadDaysBefore(dayId: Long, count: Int): List<DayStatus> {
        return calendarDao.loadDaysBefore(dayId, count.coerceAtLeast(0))
    }

    @WorkerThread
    suspend fun loadDaysAfterAndThis(dayId: Long, count: Int): List<DayStatus> {
        return calendarDao.loadDaysAfterAndThis(dayId, count.coerceAtLeast(0))
    }




}