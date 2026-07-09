package com.serj.matchuphelper.domain.repository

import com.serj.matchuphelper.domain.model.Champion
import kotlinx.coroutines.flow.Flow

interface ChampionRepository {
    fun getAllChampions(): Flow<List<Champion>>
    suspend fun getChampion(id: String): Champion?
    suspend fun searchChampions(query: String): List<Champion>
    suspend fun syncIfNewPatch()
}
