package com.yellowtubby.matchuphelper.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ChampionMatchupEntity::class], version = 2)
abstract class MatchupDatabase : RoomDatabase() {
    abstract fun matchupsDao(): MatchupDAO
}