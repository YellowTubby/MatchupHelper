package com.yellowtubby.victoryvault.di

import android.util.Log
import com.yellowtubby.victoryvault.domain.champions.ChampionListUseCase
import com.yellowtubby.victoryvault.model.Champion
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestDefinedChampionListUseCase : ChampionListUseCase {
    override suspend fun invoke(): Flow<List<Champion>> = flow {
        emit(listOf(Champion("Ahri")))
    }
}