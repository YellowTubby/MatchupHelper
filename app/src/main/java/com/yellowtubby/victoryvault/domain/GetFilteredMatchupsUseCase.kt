package com.yellowtubby.victoryvault.domain

import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import kotlinx.coroutines.flow.Flow
import org.koin.java.KoinJavaComponent.inject

class GetFilteredMatchupsUseCase {
    protected val matchupRepository: MatchupRepository by inject(MatchupRepository::class.java)

    operator suspend fun invoke(): Flow<List<Matchup>> {
        return matchupRepository.getAllMatchups()
    }
}