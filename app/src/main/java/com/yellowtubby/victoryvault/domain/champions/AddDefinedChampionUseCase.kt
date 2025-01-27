package com.yellowtubby.victoryvault.domain.champions

import android.util.Log
import com.yellowtubby.victoryvault.model.Champion

class AddDefinedChampionUseCase : BaseDefinedChampionUseCase() {
    override suspend fun invoke(champion: Champion) {
        matchupRepository.addChampion(champion).also {
            matchupRepository.updateChampion(champion)
        }
    }
}