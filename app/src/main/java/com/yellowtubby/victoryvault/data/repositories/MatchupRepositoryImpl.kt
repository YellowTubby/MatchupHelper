package com.yellowtubby.victoryvault.data.repositories

import com.yellowtubby.victoryvault.data.datasources.local.MatchupDatabase
import com.yellowtubby.victoryvault.data.datasources.local.toExternal
import com.yellowtubby.victoryvault.data.datasources.local.toLocal
import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.data.datamodels.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.java.KoinJavaComponent.inject

class MatchupRepositoryImpl : MatchupRepository {
    val db: MatchupDatabase by inject(MatchupDatabase::class.java)

    override suspend fun upsertChampion(champion: Champion) {
        db.matchupsDao().upsertChampion(champion.toLocal())
    }

    override suspend fun upsertMatchup(prevMatch: Matchup) {
        db.matchupsDao().upsertMatchup(prevMatch.toLocal())
    }

    override suspend fun getAllDefinedChampions(): Flow<List<Champion>> {
        return db.matchupsDao().getAllChampions().map {
            list -> list.map { it.toExternal() }
        }
    }

    override suspend fun getAllMatchups(): Flow<List<Matchup>> {
        return db.matchupsDao().getAllMatchups().map {
                list -> list.map { it.toExternal() }
        }
    }

    override suspend fun deleteMatchups(champion : String, role : Role, matchups: List<Matchup>) {
        db.matchupsDao().deleteMatchups(champion,role, matchups.map { it.enemy.name })
    }
}