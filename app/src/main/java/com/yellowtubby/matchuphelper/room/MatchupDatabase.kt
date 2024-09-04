package com.yellowtubby.matchuphelper.room

import androidx.room.Database
import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ChampionEntity::class, MatchupEntity::class], version = 2)
@TypeConverters(MatchupTypeConverters::class)
abstract class MatchupDatabase : RoomDatabase() {
    abstract fun matchupsDao(): MatchupDAO
}


data class ChampionMatchups(
    @Embedded val champion: ChampionEntity,
    @Relation(
        parentColumn = "champion_name",
        entityColumn = "champion_name"
    )
    val matchup: MatchupEntity
)