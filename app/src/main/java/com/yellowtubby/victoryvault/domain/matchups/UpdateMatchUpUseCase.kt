package com.yellowtubby.victoryvault.domain.matchups

import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.data.repositories.MatchupRepository
import org.koin.java.KoinJavaComponent.inject

class UpdateMatchUpUseCase {
    protected val matchupRepository: MatchupRepository by inject(MatchupRepository::class.java)
    suspend operator fun invoke(matchup: Matchup) {
        matchupRepository.upsertMatchup(matchup)
    }
}