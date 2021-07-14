package ru.clementl.metrotimex.repositories

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.room.daos.DayStatusDao
import java.time.LocalDate

class DayStatusRepository(private val dayStatusDao: DayStatusDao) {

    val allDays: Flow<List<DayStatus>> = dayStatusDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(dayStatus: DayStatus) {
        dayStatusDao.insert(dayStatus)
    }


}