package com.yellowtubby.victoryvault.domain.matchups

import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import kotlinx.coroutines.flow.Flow
import org.koin.java.KoinJavaComponent.inject

class GetFilteredMatchupsUseCase : MatchupListUseCase {
    private val matchupRepository: MatchupRepository by inject(MatchupRepository::class.java)

    override suspend operator fun invoke(): Flow<List<Matchup>> {
        return matchupRepository.getAllMatchups()
    }
}