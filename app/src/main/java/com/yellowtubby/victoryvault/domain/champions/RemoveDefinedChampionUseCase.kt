package com.yellowtubby.victoryvault.domain.champions

import com.yellowtubby.victoryvault.core.domain.BaseDefinedChampionUseCase
import com.yellowtubby.victoryvault.data.datamodels.Champion

class RemoveDefinedChampionUseCase : BaseDefinedChampionUseCase() {
    override suspend fun invoke(champion: Champion) {
    }

}