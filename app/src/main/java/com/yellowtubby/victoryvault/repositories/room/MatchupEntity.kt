package com.yellowtubby.victoryvault.repositories.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.yellowtubby.victoryvault.model.Role

@Entity(
    primaryKeys = ["champion_name","champion_enemy","role"]
)
data class MatchupEntity(
    @ColumnInfo(name = "champion_name") val championName: String,
    @ColumnInfo(name = "champion_enemy") val championEnemy: String,
    @ColumnInfo(name = "role") val role: Role,
    @ColumnInfo(name = "numWins") val numWins: Int,
    @ColumnInfo(name = "numTotal") val numTotal: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "difficulty") val difficulty: Int
)
