package com.yellowtubby.victoryvault.di

import com.yellowtubby.victoryvault.core.domain.BaseDefinedChampionUseCase
import com.yellowtubby.victoryvault.data.datamodels.Champion


class TestAddChampionUseCase constructor() : BaseDefinedChampionUseCase() {
    override suspend fun invoke(champion: Champion) {}
}