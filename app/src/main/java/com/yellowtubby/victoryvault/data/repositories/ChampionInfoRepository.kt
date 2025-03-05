package com.yellowtubby.victoryvault.data.repositories

import com.yellowtubby.victoryvault.data.datamodels.Champion
import kotlinx.coroutines.flow.Flow

interface ChampionInfoRepository {
    fun getAllChampions(): Flow<List<Champion>>
}