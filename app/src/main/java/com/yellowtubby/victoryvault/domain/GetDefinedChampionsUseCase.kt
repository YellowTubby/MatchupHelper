package com.yellowtubby.victoryvault.domain

import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import org.koin.java.KoinJavaComponent.inject

class GetDefinedChampionsUseCase {
    protected val matchupRepository: MatchupRepository by inject(MatchupRepository::class.java)


    suspend operator fun invoke(): Flow<List<Champion>> = matchupRepository.getAllDefinedChampions()

}