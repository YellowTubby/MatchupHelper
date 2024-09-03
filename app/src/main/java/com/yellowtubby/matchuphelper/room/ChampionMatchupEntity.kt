package com.yellowtubby.matchuphelper.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yellowtubby.matchuphelper.ui.model.Champion
import com.yellowtubby.matchuphelper.ui.model.Matchup

@Entity
data class ChampionMatchupEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "champion_name") val champion: Champion?,
    @ColumnInfo(name = "matchups") val matchups: List<Matchup>?
)
