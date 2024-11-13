package com.yellowtubby.victoryvault.repositories

import com.yellowtubby.victoryvault.ui.model.Ability
import com.yellowtubby.victoryvault.ui.model.Champion
import com.yellowtubby.victoryvault.ui.model.Matchup
import com.yellowtubby.victoryvault.ui.model.Role

interface MatchupRepository {
    suspend fun selectChampion(championInfoRepository: ChampionInfoRepository)
    suspend fun getAllChampions(): List<Champion>
    suspend fun getAllMatchups(): List<Matchup>
    suspend fun addMatchup(matchup: Matchup)
    suspend fun deleteMatchups(champion : String, role : Role, matchups: List<Matchup>)
    suspend fun addChampion(champion: Champion)
    suspend fun addAbility(ability: Ability)
    suspend fun updateMatchup(prevMatch: Matchup)
}