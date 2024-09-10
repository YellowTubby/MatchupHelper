package com.yellowtubby.matchuphelper.repositories

import com.yellowtubby.matchuphelper.ui.model.Ability
import com.yellowtubby.matchuphelper.ui.model.Champion
import com.yellowtubby.matchuphelper.ui.model.ChampionInformation
import com.yellowtubby.matchuphelper.ui.model.Matchup
import com.yellowtubby.matchuphelper.ui.model.Role

interface MatchupRepository {
    suspend fun selectChampion(championInfoRepository: ChampionInfoRepository)
    suspend fun getAllChampions(): List<Champion>
    suspend fun getAllMatchupsforChampion(champion: Champion): List<Matchup>
    suspend fun addMatchup(matchup: Matchup)
    suspend fun deleteMatchups(champion : String, role : Role, matchups: List<Matchup>)
    suspend fun addChampion(champion: Champion)
    suspend fun addAbility(ability: Ability)
}