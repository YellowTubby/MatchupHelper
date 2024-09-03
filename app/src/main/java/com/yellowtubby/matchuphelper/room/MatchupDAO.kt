package com.yellowtubby.matchuphelper.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yellowtubby.matchuphelper.ui.model.Champion

@Dao
interface MatchupDAO {

    @Insert
    fun insertChampion(champion: ChampionMatchupEntity)

    @Query("Select * from championMatchupEntity WHERE champion_name = :champion LIMIT 1")
    fun getSpecificChampionMatchups(champion: Champion) : ChampionMatchupEntity
}