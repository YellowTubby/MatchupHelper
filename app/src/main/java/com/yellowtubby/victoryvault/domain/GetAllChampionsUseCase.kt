package com.yellowtubby.victoryvault.domain

import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.repositories.ChampionInfoRepository
import kotlinx.coroutines.flow.Flow

class GetAllChampionsUseCase(
    private val championInfoRepository: ChampionInfoRepository
) {
    operator fun invoke(): Flow<List<Champion>> {
        return championInfoRepository.getAllChampions()
    }

}