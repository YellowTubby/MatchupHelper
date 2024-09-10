package com.yellowtubby.matchuphelper.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ChampionEntity::class, MatchupEntity::class, AbilityEntity::class], version = 1)
@TypeConverters(MatchupTypeConverters::class)
abstract class MatchupDatabase : RoomDatabase() {
    abstract fun matchupsDao(): MatchupDAO
}
