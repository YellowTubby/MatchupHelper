package com.yellowtubby.victoryvault.data.datasources.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.yellowtubby.victoryvault.data.datamodels.Role
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchupDAO {

    @Upsert
    suspend fun upsertChampion(champion: LocalChampion)

    @Upsert
    suspend fun upsertMatchup(matchup: LocalMatchup)

    @Query("Select * from localmatchup")
    fun getAllMatchups() : Flow<List<LocalMatchup>>

    @Query("Select * from localchampion")
    fun getAllChampions() : Flow<List<LocalChampion>>

    @Query("DELETE FROM localmatchup WHERE champion_name = :champion_name AND role = :role AND champion_enemy IN (:names)")
    suspend fun deleteMatchups(champion_name : String, role: Role, names: List<String>)

    @Query("DELETE FROM localchampion WHERE champion_name = :champion_name")
    suspend fun deleteChampion(champion_name: String)


}