package ru.clementl.metrotimex.repositories

import androidx.lifecycle.LiveData
import ru.clementl.metrotimex.model.norma.YearMonthData
import ru.clementl.metrotimex.model.room.daos.YearMonthDataDao

class YearMonthRepository(private val yearMonthDataDao: YearMonthDataDao) {

    suspend fun insert(yearMonthData: YearMonthData) {
        yearMonthDataDao.insert(yearMonthData)
    }

    suspend fun getAll(): List<YearMonthData> {
        return yearMonthDataDao.getAll()
    }

    fun getAllAsLiveData(): LiveData<List<YearMonthData>> {
        return yearMonthDataDao.getAllAsLiveData()
    }

}