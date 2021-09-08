package ru.clementl.metrotimex.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.clementl.metrotimex.model.data.MachinistStatus
import ru.clementl.metrotimex.model.room.daos.MachinistStatusChangeDao

class MachinistStatusRepository(private val machinistStatusDao: MachinistStatusChangeDao) {

    val allStatusFlow: Flow<List<MachinistStatus>> = machinistStatusDao.getAllAsFlow()

    @WorkerThread
    suspend fun getAll(): List<MachinistStatus> {
        return machinistStatusDao.getAll()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(machinistStatus: MachinistStatus) {
        machinistStatusDao.insert(machinistStatus)
    }

    fun getAllAsLiveData(): LiveData<List<MachinistStatus>> {
        return machinistStatusDao.getAllAsLiveData()
    }
}