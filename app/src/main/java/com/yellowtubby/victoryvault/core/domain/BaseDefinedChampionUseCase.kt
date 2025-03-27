package com.yellowtubby.victoryvault.core.domain

import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.repositories.MatchupRepository
import org.koin.java.KoinJavaComponent

abstract class BaseDefinedChampionUseCase {
    protected val matchupRepository: MatchupRepository by KoinJavaComponent.inject(MatchupRepository::class.java)
    abstract suspend operator fun invoke(champion: Champion)
}