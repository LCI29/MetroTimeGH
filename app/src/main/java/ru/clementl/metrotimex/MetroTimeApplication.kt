package ru.clementl.metrotimex

import android.app.Application
import androidx.preference.PreferenceManager
import ru.clementl.metrotimex.model.room.MetroTimeDatabase
import ru.clementl.metrotimex.repositories.CalendarRepository
import ru.clementl.metrotimex.repositories.MachinistStatusRepository
import ru.clementl.metrotimex.repositories.YearMonthRepository

class MetroTimeApplication : Application() {

    val database by lazy { MetroTimeDatabase.getDatabase(this) }
    val repository by lazy { CalendarRepository(database.dayStatusDao()) }
    val machinistStatusRepository by lazy { MachinistStatusRepository(database.machinistStatusChangeDao()) }
    val yearMonthRepository by lazy { YearMonthRepository(database.yearMonthDataDao()) }
    val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(baseContext) }
}