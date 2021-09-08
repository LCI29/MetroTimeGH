package ru.clementl.metrotimex.model.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.clementl.metrotimex.RATE_PER_HOUR_DEFAULT
import ru.clementl.metrotimex.converters.toLong
import java.time.LocalDate

@Entity(tableName = "machinist_status_change_table")
data class MachinistStatus(
    @PrimaryKey @NonNull @ColumnInfo(name = "date") val date: Long,
    @NonNull @ColumnInfo(name = "on_post_since") val onPostSince: Long,
    @NonNull @ColumnInfo(name = "qualification_class") val qualificationClass: Int,
    @NonNull @ColumnInfo(name = "is_master") val isMaster: Int,
    @NonNull @ColumnInfo(name = "is_mentor") val isMentor: Int,
    @NonNull @ColumnInfo(name = "month_bonus") val monthBonus: Int,
    @NonNull @ColumnInfo(name = "in_union") val inUnion: Int,
    @NonNull @ColumnInfo(name = "rate_per_hour") val ratePerHour: Double
) {
    companion object {
        fun from(
            machinist: Machinist,
            date: Long = LocalDate.now().toLong(),
            ratePerHour: Double = RATE_PER_HOUR_DEFAULT
        ): MachinistStatus {
            return MachinistStatus(
                date = date,
                onPostSince = machinist.onPostSince.toLong(),
                qualificationClass = machinist.qualificationClass,
                isMaster = if (machinist.isMaster) 1 else 0,
                isMentor = if (machinist.isMentor) 1 else 0,
                monthBonus = (machinist.monthBonus * 100).toInt(),
                inUnion = if (machinist.isInUnion) 1 else 0,
                ratePerHour = ratePerHour
            )
        }
    }
}