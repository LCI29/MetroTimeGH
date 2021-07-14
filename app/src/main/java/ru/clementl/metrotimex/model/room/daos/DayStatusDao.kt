package ru.clementl.metrotimex.model.room.daos

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.clementl.metrotimex.model.data.DayStatus
import java.time.LocalDate

@Dao
interface DayStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dayStatus: DayStatus)

    @Query("SELECT * FROM calendar ORDER BY date")
    fun getAll(): Flow<List<DayStatus>>


}