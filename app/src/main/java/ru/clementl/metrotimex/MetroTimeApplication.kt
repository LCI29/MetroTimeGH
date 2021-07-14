package ru.clementl.metrotimex

import android.app.Application
import ru.clementl.metrotimex.model.room.MetroTimeDatabase
import ru.clementl.metrotimex.repositories.DayStatusRepository

class MetroTimeApplication : Application() {

    val database by lazy { MetroTimeDatabase.getDatabase(this) }
    val repository by lazy { DayStatusRepository(database.dayStatusDao()) }
}