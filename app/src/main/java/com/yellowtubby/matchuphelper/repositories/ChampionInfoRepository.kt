package com.yellowtubby.matchuphelper.repositories

import com.yellowtubby.matchuphelper.ui.model.Champion

interface ChampionInfoRepository {
    suspend fun getAllChampions(): List<Champion>
}