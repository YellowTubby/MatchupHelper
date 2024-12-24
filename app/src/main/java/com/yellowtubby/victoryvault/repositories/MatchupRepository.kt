package com.yellowtubby.victoryvault.repositories

import com.yellowtubby.victoryvault.model.Ability
import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.model.Role

interface MatchupRepository {
    suspend fun getAllChampions(): List<Champion>
    suspend fun getAllMatchups(): List<Matchup>
    suspend fun addMatchup(matchup: Matchup)
    suspend fun deleteMatchups(champion : String, role : Role, matchups: List<Matchup>)
    suspend fun addChampion(champion: Champion)
    suspend fun addAbility(ability: Ability)
    suspend fun updateMatchup(prevMatch: Matchup)
}