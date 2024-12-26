package com.yellowtubby.victoryvault.repositories

import com.yellowtubby.victoryvault.model.Champion
import kotlinx.coroutines.flow.Flow

interface ChampionInfoRepository {
    fun getAllChampions(): Flow<List<Champion>>
}