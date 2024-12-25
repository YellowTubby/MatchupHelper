package com.yellowtubby.victoryvault.domain

import com.yellowtubby.victoryvault.model.Champion

class AddDefinedChampionUseCase : BaseDefinedChampionUseCase() {
    override suspend fun invoke(champion: Champion) {
        matchupRepository.addChampion(champion)
    }
}