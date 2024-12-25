package com.yellowtubby.victoryvault.domain

import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.model.Role
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import org.koin.java.KoinJavaComponent.inject

class RemoveMatchUpsUseCase {
    protected val matchupRepository: MatchupRepository by inject(MatchupRepository::class.java)

    suspend operator fun invoke(champion: Champion, role: Role, matchups: List<Matchup>) {
        matchupRepository.deleteMatchups(champion.name,role,matchups)
    }
}