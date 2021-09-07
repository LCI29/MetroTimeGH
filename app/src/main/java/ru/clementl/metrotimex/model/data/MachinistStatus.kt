package ru.clementl.metrotimex.model.data

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

data class MachinistStatus(
    @PrimaryKey @NonNull val date: Long,
    @NonNull val onPostSince: Long,
    @NonNull val qualificationClass: Int,
    @NonNull val isMaster: Int,
    @NonNull val isMentor: Int,
    @NonNull val monthBonus: Int,
    @NonNull val inUnion: Int
) {
}