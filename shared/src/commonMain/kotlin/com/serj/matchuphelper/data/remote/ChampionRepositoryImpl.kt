package com.serj.matchuphelper.data.remote

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.serj.matchuphelper.db.MatchupDatabase
import com.serj.matchuphelper.domain.model.Champion
import com.serj.matchuphelper.domain.model.Role
import com.serj.matchuphelper.domain.repository.ChampionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChampionRepositoryImpl(
    private val database: MatchupDatabase,
    private val dataDragonApi: DataDragonApi,
    private val patchStore: PatchVersionStore,
) : ChampionRepository {

    private val queries get() = database.championQueries

    override fun getAllChampions(): Flow<List<Champion>> {
        return queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getChampion(id: String): Champion? {
        return queries.selectById(id).executeAsOneOrNull()?.toDomain()
    }

    override suspend fun searchChampions(query: String): List<Champion> {
        return queries.search(query).executeAsList().map { it.toDomain() }
    }

    override suspend fun syncIfNewPatch() {
        val latestVersion = dataDragonApi.getLatestVersion()
        val cachedVersion = patchStore.getCachedPatchVersion()

        if (cachedVersion == latestVersion) return

        val champions = dataDragonApi.getChampions(latestVersion)

        database.transaction {
            queries.deleteAll()
            champions.forEach { champ ->
                val roles = tagToRoles(champ.tags).joinToString(",") { it.name }
                val imageUrl = DataDragonApi.championImageUrl(latestVersion, champ.imageFileName)
                queries.insert(champ.id, champ.name, champ.title, roles, imageUrl)
            }
        }

        patchStore.savePatchVersion(latestVersion)
    }
}

private fun tagToRoles(tags: List<String>): List<Role> {
    return tags.mapNotNull { tag ->
        when (tag) {
            "Fighter", "Tank" -> Role.TOP
            "Mage" -> Role.MID
            "Assassin" -> Role.MID
            "Marksman" -> Role.BOT
            "Support" -> Role.SUPPORT
            else -> null
        }
    }.distinct()
}

private fun com.serj.matchuphelper.db.Champion.toDomain(): Champion {
    return Champion(
        id = id,
        name = name,
        title = title,
        roles = roles.split(",").mapNotNull { roleName ->
            Role.entries.find { it.name == roleName }
        },
        imageUrl = imageUrl,
    )
}
