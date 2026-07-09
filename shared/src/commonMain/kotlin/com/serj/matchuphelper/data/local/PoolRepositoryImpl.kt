package com.serj.matchuphelper.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.serj.matchuphelper.db.MatchupDatabase
import com.serj.matchuphelper.domain.model.PoolEntry
import com.serj.matchuphelper.domain.model.Role
import com.serj.matchuphelper.domain.repository.PoolRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PoolRepositoryImpl(
    private val database: MatchupDatabase,
) : PoolRepository {

    private val queries get() = database.poolEntryQueries

    override fun getAllEntries(): Flow<List<PoolEntry>> {
        return queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun getEntriesByRole(role: Role): Flow<List<PoolEntry>> {
        return queries.selectByRole(role.name)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addToPool(championId: String, role: Role, comfort: Int) {
        queries.insert(championId, role.name, comfort.toLong())
    }

    override suspend fun removeFromPool(championId: String, role: Role) {
        queries.delete(championId, role.name)
    }

    override suspend fun updateComfort(championId: String, role: Role, comfort: Int) {
        queries.updateComfort(comfort.toLong(), championId, role.name)
    }

    private fun com.serj.matchuphelper.db.PoolEntry.toDomain(): PoolEntry {
        return PoolEntry(
            championId = championId,
            role = Role.valueOf(role),
            comfort = comfort.toInt(),
        )
    }
}
