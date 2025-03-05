package com.yellowtubby.victoryvault.domain.champions

import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.repositories.ChampionInfoRepository
import kotlinx.coroutines.flow.Flow

class GetAllChampionsUseCase(
    private val championInfoRepository: ChampionInfoRepository
) : ChampionListUseCase {
    override suspend operator fun invoke(): Flow<List<Champion>> {
        return championInfoRepository.getAllChampions()
    }

}