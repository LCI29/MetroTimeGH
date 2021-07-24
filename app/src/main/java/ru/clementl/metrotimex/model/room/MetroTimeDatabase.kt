package ru.clementl.metrotimex.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.Shift
import ru.clementl.metrotimex.model.room.daos.CalendarDao

@Database(entities = arrayOf(Shift::class, DayStatus::class), version = 2, exportSchema = false)
abstract class MetroTimeDatabase : RoomDatabase() {

    abstract fun dayStatusDao(): CalendarDao

    companion object {
        @Volatile
        private var INSTANCE: MetroTimeDatabase? = null

        fun getDatabase(context: Context): MetroTimeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MetroTimeDatabase::class.java,
                    "metro_time_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}