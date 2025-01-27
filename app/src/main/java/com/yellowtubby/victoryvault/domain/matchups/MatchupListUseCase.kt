package com.yellowtubby.victoryvault.domain.matchups

import com.yellowtubby.victoryvault.model.Matchup
import kotlinx.coroutines.flow.Flow

interface MatchupListUseCase {
    suspend operator fun invoke(): Flow<List<Matchup>>
}