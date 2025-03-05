package com.yellowtubby.victoryvault.data.repositories

import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.data.datamodels.Role
import kotlinx.coroutines.flow.Flow

interface MatchupRepository {
    suspend fun getAllDefinedChampions(): Flow<List<Champion>>
    suspend fun getAllMatchups(): Flow<List<Matchup>>
    suspend fun deleteMatchups(champion : String, role : Role, matchups: List<Matchup>)
    suspend fun upsertChampion(champion: Champion)
    suspend fun upsertMatchup(prevMatch: Matchup)
}