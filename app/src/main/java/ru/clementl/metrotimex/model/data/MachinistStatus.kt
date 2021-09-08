package ru.clementl.metrotimex.model.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import ru.clementl.metrotimex.ATZ_FACTOR
import ru.clementl.metrotimex.HOUR_MILLI
import ru.clementl.metrotimex.RATE_PER_HOUR_DEFAULT
import ru.clementl.metrotimex.RESERVE_LIGHT_Q
import ru.clementl.metrotimex.converters.toDate
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
        fun create(
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

    @Ignore val machinist: Machinist = Machinist(
            onPostSince.toDate(),
            qualificationClass,
            isMaster != 0,
            isMentor != 0,
            monthBonus / 100.0,
            inUnion != 0
        )

    @Ignore val ratePerMilli = ratePerHour / HOUR_MILLI
    @Ignore val rateReserveLight = RESERVE_LIGHT_Q * ratePerHour
    @Ignore val rateReserveLightMilli = rateReserveLight / HOUR_MILLI
    @Ignore val rateAtzShift = ATZ_FACTOR * (ratePerHour - rateReserveLight) + rateReserveLight
    @Ignore val rateAtzShiftPerMilli = rateAtzShift / HOUR_MILLI

}