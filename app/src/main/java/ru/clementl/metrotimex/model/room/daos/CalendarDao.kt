package ru.clementl.metrotimex.model.room.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.clementl.metrotimex.model.data.DayStatus
import java.time.LocalDate

@Dao
interface CalendarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dayStatus: DayStatus)

    @Query("SELECT * FROM calendar_table WHERE date = :date")
    fun getLiveDayByDate(date: Long): LiveData<DayStatus>

    @Query("SELECT * FROM calendar_table WHERE date = :date")
    suspend fun getDayByDate(date: Long): DayStatus?

    @Query("SELECT * FROM calendar_table ORDER BY date")
    fun getAll(): Flow<List<DayStatus>>

    @Query("SELECT MAX(date) FROM calendar_table")
    suspend fun getLastDateLong(): Long?

    @Query("DELETE FROM calendar_table WHERE date = :dayId")
    suspend fun deleteDayById(dayId: Long)

    @Query("SELECT * FROM calendar_table WHERE date < :dayId ORDER BY date DESC LIMIT :count")
    suspend fun loadDaysBefore(dayId: Long, count: Int): List<DayStatus>

    @Query("SELECT * FROM calendar_table WHERE date >= :dayId ORDER BY date LIMIT :count")
    suspend fun loadDaysAfterAndThis(dayId: Long, count: Int): List<DayStatus>




}