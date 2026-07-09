package com.serj.matchuphelper.domain.repository

import com.serj.matchuphelper.domain.model.PoolEntry
import com.serj.matchuphelper.domain.model.Role
import kotlinx.coroutines.flow.Flow

interface PoolRepository {
    fun getAllEntries(): Flow<List<PoolEntry>>
    fun getEntriesByRole(role: Role): Flow<List<PoolEntry>>
    suspend fun addToPool(championId: String, role: Role, comfort: Int = 3)
    suspend fun removeFromPool(championId: String, role: Role)
    suspend fun updateComfort(championId: String, role: Role, comfort: Int)
}
