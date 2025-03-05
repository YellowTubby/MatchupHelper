package com.yellowtubby.victoryvault.data.datasources.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yellowtubby.victoryvault.data.datamodels.Champion

@Entity
data class LocalChampion(
    @PrimaryKey val champion_name: String,
)


fun LocalChampion.toExternal() = Champion(champion_name)

fun List<LocalChampion>.toExternal() = map(LocalChampion::toExternal)


fun Champion.toLocal() = LocalChampion(champion_name = name)

