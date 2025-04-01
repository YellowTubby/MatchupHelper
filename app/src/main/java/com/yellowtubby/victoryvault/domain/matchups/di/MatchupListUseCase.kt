package com.yellowtubby.victoryvault.domain.matchups.di

import com.yellowtubby.victoryvault.data.datamodels.Matchup
import kotlinx.coroutines.flow.Flow

interface MatchupListUseCase {
    suspend operator fun invoke(): Flow<List<Matchup>>
}