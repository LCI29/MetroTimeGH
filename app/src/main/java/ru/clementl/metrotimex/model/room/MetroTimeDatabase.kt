package ru.clementl.metrotimex.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.clementl.metrotimex.model.data.DayStatus
import ru.clementl.metrotimex.model.data.MachinistStatus
import ru.clementl.metrotimex.model.data.Shift
import ru.clementl.metrotimex.model.norma.YearMonthData
import ru.clementl.metrotimex.model.room.daos.CalendarDao
import ru.clementl.metrotimex.model.room.daos.MachinistStatusChangeDao
import ru.clementl.metrotimex.model.room.daos.YearMonthDataDao
import ru.clementl.metrotimex.model.room.migration.*

@Database(
    entities = [Shift::class, DayStatus::class, MachinistStatus::class, YearMonthData::class],
    version = 10
)
abstract class MetroTimeDatabase : RoomDatabase() {

    abstract fun dayStatusDao(): CalendarDao
    abstract fun machinistStatusChangeDao(): MachinistStatusChangeDao
    abstract fun yearMonthDataDao(): YearMonthDataDao

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
                    .addMigrations(MIGRATION_5_6)
                    .addMigrations(MIGRATION_6_7)
                    .addMigrations(MIGRATION_7_8)
                    .addMigrations(MIGRATION_8_9)
                    .addMigrations(MIGRATION_9_10)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}