package com.yellowtubby.victoryvault.data.datasources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [LocalChampion::class, LocalMatchup::class], version = 2)
@TypeConverters(MatchupTypeConverters::class)
abstract class MatchupDatabase : RoomDatabase() {
    abstract fun matchupsDao(): MatchupDAO
}
