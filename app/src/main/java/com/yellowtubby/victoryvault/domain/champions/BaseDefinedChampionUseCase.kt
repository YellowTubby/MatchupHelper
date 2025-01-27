package com.yellowtubby.victoryvault.domain.champions

import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import org.koin.java.KoinJavaComponent.inject


abstract class BaseDefinedChampionUseCase {
    protected val matchupRepository: MatchupRepository by inject(MatchupRepository::class.java)
    abstract suspend operator fun invoke(champion: Champion)
}