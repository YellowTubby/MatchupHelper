package com.yellowtubby.victoryvault.domain

import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import org.koin.java.KoinJavaComponent.inject

class UpdateMatchUpUseCase {
    protected val matchupRepository: MatchupRepository by inject(MatchupRepository::class.java)
    suspend operator fun invoke(matchup: Matchup) {
        matchupRepository.updateMatchup(matchup)
    }
}