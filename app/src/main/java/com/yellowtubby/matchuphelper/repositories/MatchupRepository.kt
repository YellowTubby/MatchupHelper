package com.yellowtubby.matchuphelper.repositories

import com.yellowtubby.matchuphelper.ui.model.Champion
import com.yellowtubby.matchuphelper.ui.model.Matchup

interface MatchupRepository {
    suspend fun selectChampion(championInfoRepository: ChampionInfoRepository)
    suspend fun addChampion(champion: Champion)
    suspend fun getAllChampions(): List<Champion>
    suspend fun getAllMatchupsforChampion(champion: Champion): List<Matchup>
    suspend fun addMatchup(matchup: Matchup)
}