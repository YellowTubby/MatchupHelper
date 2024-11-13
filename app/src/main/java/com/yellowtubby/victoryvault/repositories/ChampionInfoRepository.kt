package com.yellowtubby.victoryvault.repositories

import com.yellowtubby.victoryvault.ui.model.Champion

interface ChampionInfoRepository {
    suspend fun getAllChampions(): List<Champion>
}