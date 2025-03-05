package com.yellowtubby.victoryvault.domain.champions

import com.yellowtubby.victoryvault.data.datamodels.Champion

class AddDefinedChampionUseCase : BaseDefinedChampionUseCase() {
    override suspend fun invoke(champion: Champion) {
        matchupRepository.upsertChampion(champion).also {
            matchupRepository.upsertChampion(champion)
        }
    }
}