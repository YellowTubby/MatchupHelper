package com.yellowtubby.matchuphelper.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.yellowtubby.matchuphelper.ui.model.Ability
import com.yellowtubby.matchuphelper.ui.model.Role

@Dao
interface MatchupDAO {

    @Insert
    suspend fun insertChampion(champion: ChampionEntity)

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertMatchup(matchup: MatchupEntity)

    @Insert
    suspend fun insertAbility(ability: AbilityEntity)

    @Query("Select * from matchupentity WHERE champion_name = :champion")
    suspend fun getSpecificChampionMatchups(champion: String) : List<MatchupEntity>

    @Query("Select * from championentity")
    suspend fun getAllChampions() : List<ChampionEntity>

    @Update
    suspend fun updateChampion(champion: ChampionEntity)

    @Query("DELETE FROM matchupentity WHERE champion_name = :champion_name AND role = :role AND champion_enemy IN (:names)")
    suspend fun deleteMatchups(champion_name : String, role: Role, names: List<String>)

    @Query("DELETE FROM championentity WHERE champion_name = :champion_name")
    suspend fun deleteChampion(champion_name: String)

    @Transaction
    @Query("SELECT * FROM championentity WHERE champion_name = :champion_name")
    fun getChampionAbilities(champion_name: String): List<ChampionAbilities>

}