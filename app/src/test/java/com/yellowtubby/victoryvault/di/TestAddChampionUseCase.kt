package com.yellowtubby.victoryvault.di

import android.util.Log
import com.yellowtubby.victoryvault.domain.champions.BaseDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.champions.ChampionListUseCase
import com.yellowtubby.victoryvault.model.Champion


class TestAddChampionUseCase constructor() : BaseDefinedChampionUseCase() {
    override suspend fun invoke(champion: Champion) {}
}