package ru.clementl.metrotimex.room.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.clementl.metrotimex.model.data.MachinistStatus

@Dao
interface MachinistStatusChangeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(machinistStatus: MachinistStatus)


    @Query("SELECT * FROM machinist_status_change_table ORDER BY date")
    fun getAllAsFlow(): Flow<List<MachinistStatus>>

    @Query("SELECT * FROM machinist_status_change_table ORDER BY date")
    fun getAllAsLiveData(): LiveData<List<MachinistStatus>>

    @Query("SELECT * FROM machinist_status_change_table ORDER BY date")
    suspend fun getAll(): List<MachinistStatus>


}