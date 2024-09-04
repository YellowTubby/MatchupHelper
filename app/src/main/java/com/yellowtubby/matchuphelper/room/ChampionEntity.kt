package com.yellowtubby.matchuphelper.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yellowtubby.matchuphelper.ui.model.Matchup

@Entity
data class ChampionEntity(
    @PrimaryKey val champion_name: String
)
