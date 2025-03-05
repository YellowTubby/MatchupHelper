package com.yellowtubby.victoryvault.data.datasources.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.data.datamodels.Role

@Entity(
    primaryKeys = ["champion_name","champion_enemy","role"]
)
data class LocalMatchup(
    @ColumnInfo(name = "champion_name") val championName: String,
    @ColumnInfo(name = "champion_enemy") val championEnemy: String,
    @ColumnInfo(name = "role") val role: Role,
    @ColumnInfo(name = "numWins") val numWins: Int,
    @ColumnInfo(name = "numTotal") val numTotal: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "difficulty") val difficulty: Int
)

fun LocalMatchup.toExternal() = Matchup(
        orig = Champion(championName),
        enemy = Champion(championEnemy),
        role = role,
        numWins = numWins,
)

fun List<LocalMatchup>.toExternal() = map(LocalMatchup::toExternal)


fun Matchup.toLocal() = LocalMatchup(
    championName = orig.name,
    championEnemy = enemy.name,
    role = role,
    numWins = numWins,
    numTotal = numTotal,
    description = description,
    difficulty = difficulty
)