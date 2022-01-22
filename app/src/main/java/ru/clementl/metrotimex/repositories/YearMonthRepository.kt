package ru.clementl.metrotimex.repositories

import androidx.lifecycle.LiveData
import ru.clementl.metrotimex.room.daos.YearMonthDataDao
import ru.clementl.metrotimex.screens.norma.YearMonthData

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