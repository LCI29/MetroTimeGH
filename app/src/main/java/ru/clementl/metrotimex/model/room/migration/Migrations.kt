package ru.clementl.metrotimex.model.room.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_5_6: Migration = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE 'machinist_status_change_table' ('date' INTEGER PRIMARY KEY NOT NULL,  'on_post_since' INTEGER NOT NULL DEFAULT 0, 'qualification_class' INTEGER NOT NULL DEFAULT 0, 'is_master' INTEGER NOT NULL DEFAULT 0, 'is_mentor' INTEGER NOT NULL DEFAULT 0, 'month_bonus' INTEGER NOT NULL DEFAULT 0, 'in_union' INTEGER NOT NULL DEFAULT 0)"
        )
    }
}

val MIGRATION_6_7: Migration = object : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE 'machinist_status_change_table' ADD COLUMN 'rate_per_hour' REAL NOT NULL DEFAULT 390.43"
        )
    }
}

val MIGRATION_7_8: Migration = object : Migration(7, 8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE 'calendar_table' ADD COLUMN 'day_notes' TEXT"
        )
    }
}

val MIGRATION_8_9: Migration = object : Migration(8, 9) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE 'calendar_table' ADD COLUMN 'tech' INTEGER"
        )
    }
}