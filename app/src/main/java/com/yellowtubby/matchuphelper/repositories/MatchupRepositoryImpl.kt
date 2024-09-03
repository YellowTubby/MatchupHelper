package com.yellowtubby.matchuphelper.repositories

import com.yellowtubby.matchuphelper.room.ChampionMatchupEntity
import com.yellowtubby.matchuphelper.room.MatchupDatabase
import com.yellowtubby.matchuphelper.ui.model.Champion
import org.koin.java.KoinJavaComponent.inject

class MatchupRepositoryImpl : MatchupRepository {
    val db: MatchupDatabase by inject(MatchupDatabase::class.java)

    override suspend fun selectChampion(championInfoRepository: ChampionInfoRepository) {

    }

    override suspend fun addChampion(champion: Champion) {
        db.matchupsDao().insertChampion(
            ChampionMatchupEntity(
                uid = 0,
                champion = champion,
                matchups = emptyList()
            )
        )
    }
}