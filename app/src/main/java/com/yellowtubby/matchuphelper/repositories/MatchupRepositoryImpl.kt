package com.yellowtubby.matchuphelper.repositories

import com.yellowtubby.matchuphelper.room.ChampionEntity
import com.yellowtubby.matchuphelper.room.MatchupDatabase
import com.yellowtubby.matchuphelper.room.MatchupEntity
import com.yellowtubby.matchuphelper.ui.model.Champion
import com.yellowtubby.matchuphelper.ui.model.Matchup
import org.koin.java.KoinJavaComponent.inject

class MatchupRepositoryImpl : MatchupRepository {
    val db: MatchupDatabase by inject(MatchupDatabase::class.java)

    override suspend fun selectChampion(championInfoRepository: ChampionInfoRepository) {

    }

    override suspend fun addChampion(champion: Champion) {
        db.matchupsDao().insertChampion(
            ChampionEntity(
                champion.name
            )
        )
    }

    override suspend fun getAllChampions(): List<Champion> {
        return db.matchupsDao().getAllChampions().map {
            Champion(it.champion_name)
        }
    }

    override suspend fun getAllMatchupsforChampion(champion: Champion) : List<Matchup> {
        return db.matchupsDao().getSpecificChampionMatchups(champion.name).map { it.matchup }.map {
            Matchup(
                orig = Champion(it.championName),
                enemy = Champion(it.championEnemy),
                role = it.role,
                difficulty = it.difficulty,
                description = it.description
            )
        }
    }

    override suspend fun addMatchup(matchup: Matchup) {
        db.matchupsDao().insertMatchup(
            MatchupEntity(
                championName = matchup.orig.name,
                championEnemy = matchup.enemy.name,
                role = matchup.role,
                difficulty = matchup.difficulty,
                description = matchup.description
            )
        )
    }
}