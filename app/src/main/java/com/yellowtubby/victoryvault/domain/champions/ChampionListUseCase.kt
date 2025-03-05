package com.yellowtubby.victoryvault.domain.champions

import com.yellowtubby.victoryvault.data.datamodels.Champion
import kotlinx.coroutines.flow.Flow

interface ChampionListUseCase {
    suspend operator fun invoke(): Flow<List<Champion>>
}