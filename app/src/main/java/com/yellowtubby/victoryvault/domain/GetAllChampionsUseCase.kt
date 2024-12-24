package com.yellowtubby.victoryvault.domain

import android.util.Log
import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.repositories.ChampionInfoRepository
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class GetAllChampionsUseCase {
    protected val championInfoRepository: ChampionInfoRepository by inject(ChampionInfoRepository::class.java)

    operator fun invoke(): Flow<List<Champion>> = flow {
        val mutableList = mutableListOf<Champion>()
        mutableList.addAll(championInfoRepository.getAllChampions())
        emit(mutableList.toList())
    }

}