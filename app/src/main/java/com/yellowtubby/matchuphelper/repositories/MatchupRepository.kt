package com.yellowtubby.matchuphelper.repositories

import com.yellowtubby.matchuphelper.ui.model.Champion

interface MatchupRepository {
    suspend fun selectChampion(championInfoRepository: ChampionInfoRepository)
    suspend fun addChampion(champion: Champion)
}