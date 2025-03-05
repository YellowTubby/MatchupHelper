package com.yellowtubby.victoryvault.di

import com.yellowtubby.victoryvault.domain.champions.BaseDefinedChampionUseCase
import com.yellowtubby.victoryvault.data.datamodels.Champion


class TestAddChampionUseCase constructor() : BaseDefinedChampionUseCase() {
    override suspend fun invoke(champion: Champion) {}
}