package com.yellowtubby.matchuphelper.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.yellowtubby.matchuphelper.ui.model.Role

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ChampionEntity::class,
            parentColumns = ["champion_name"],
            childColumns = ["champion_name"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE)
    ]
)
data class MatchupEntity(

    @ColumnInfo(name = "champion_name") val championName: String,
    @PrimaryKey
    @ColumnInfo(name = "champion_enemy") val championEnemy: String,
    @ColumnInfo(name = "role") val role: Role,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "difficulty") val difficulty: Int
)
