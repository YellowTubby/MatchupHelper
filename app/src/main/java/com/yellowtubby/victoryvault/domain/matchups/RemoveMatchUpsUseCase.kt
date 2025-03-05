package com.yellowtubby.victoryvault.domain.matchups

import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.data.datamodels.Role
import com.yellowtubby.victoryvault.data.repositories.MatchupRepository
import org.koin.java.KoinJavaComponent.inject

open class RemoveMatchUpsUseCase {
    protected val matchupRepository: MatchupRepository by inject(MatchupRepository::class.java)

    suspend operator fun invoke(champion: Champion, role: Role, matchups: List<Matchup>) {
        matchupRepository.deleteMatchups(champion.name,role,matchups)
    }
}