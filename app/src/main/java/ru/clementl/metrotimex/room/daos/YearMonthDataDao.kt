package ru.clementl.metrotimex.room.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.clementl.metrotimex.screens.norma.YearMonthData

@Dao
interface YearMonthDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: YearMonthData)

    @Query("SELECT * FROM year_month_data_table ORDER BY year_month")
    suspend fun getAll(): List<YearMonthData>

    @Query("SELECT * FROM year_month_data_table ORDER BY year_month")
    fun getAllAsLiveData(): LiveData<List<YearMonthData>>


}