package com.yellowtubby.victoryvault.repositories

import com.yellowtubby.victoryvault.model.Ability
import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.model.Role
import kotlinx.coroutines.flow.Flow

interface MatchupRepository {
    suspend fun getAllDefinedChampions(): Flow<List<Champion>>
    suspend fun getAllMatchups(): Flow<List<Matchup>>
    suspend fun addMatchup(matchup: Matchup)
    suspend fun deleteMatchups(champion : String, role : Role, matchups: List<Matchup>)
    suspend fun addChampion(champion: Champion)
    suspend fun updateChampion(champion: Champion)
    suspend fun addAbility(ability: Ability)
    suspend fun updateMatchup(prevMatch: Matchup)
}