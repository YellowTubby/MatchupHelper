package com.yellowtubby.victoryvault.domain.champions

import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.repositories.ChampionInfoRepository
import kotlinx.coroutines.flow.Flow

class GetAllChampionsUseCase(
    private val championInfoRepository: ChampionInfoRepository
) : ChampionListUseCase {
    override suspend operator fun invoke(): Flow<List<Champion>> {
        return championInfoRepository.getAllChampions()
    }

}