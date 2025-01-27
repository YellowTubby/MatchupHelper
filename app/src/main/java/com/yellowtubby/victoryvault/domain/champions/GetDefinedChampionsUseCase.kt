package com.yellowtubby.victoryvault.domain.champions

import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import kotlinx.coroutines.flow.Flow
import org.koin.java.KoinJavaComponent.inject

class GetDefinedChampionsUseCase : ChampionListUseCase {
    protected val matchupRepository: MatchupRepository by inject(MatchupRepository::class.java)


    override suspend operator fun invoke(): Flow<List<Champion>> = matchupRepository.getAllDefinedChampions()

}