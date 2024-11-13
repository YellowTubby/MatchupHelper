package com.yellowtubby.victoryvault.repositories

import com.yellowtubby.victoryvault.room.AbilityEntity
import com.yellowtubby.victoryvault.room.ChampionEntity
import com.yellowtubby.victoryvault.room.LIST_DELIMITER
import com.yellowtubby.victoryvault.room.MatchupDatabase
import com.yellowtubby.victoryvault.room.MatchupEntity
import com.yellowtubby.victoryvault.ui.model.Ability
import com.yellowtubby.victoryvault.ui.model.Champion
import com.yellowtubby.victoryvault.ui.model.Matchup
import com.yellowtubby.victoryvault.ui.model.Role
import org.koin.java.KoinJavaComponent.inject

class MatchupRepositoryImpl : MatchupRepository {
    val db: MatchupDatabase by inject(MatchupDatabase::class.java)

    override suspend fun selectChampion(championInfoRepository: ChampionInfoRepository) {

    }

    override suspend fun addChampion(champion: Champion) {
        db.matchupsDao().insertChampion(
            ChampionEntity(
                champion.name,
            )
        )
    }

    override suspend fun addAbility(ability: Ability) {
        db.matchupsDao().insertAbility(
            AbilityEntity(
                ability_id = ability.championName + ability.type.name,
                championName = ability.championName,
                abilityName = ability.abilityName,
                abilityType = ability.type.name,
                cooldowns = ability.cooldownList.joinToString(LIST_DELIMITER),
                damageOrShieldList = ability.damageOrShieldList.joinToString(LIST_DELIMITER),
                modifier = ability.modifier
            )
        )
    }

    override suspend fun getAllChampions(): List<Champion> {
        return db.matchupsDao().getAllChampions().map {
            Champion(it.champion_name)
        }
    }

    override suspend fun getAllMatchupsforChampion(champion: Champion) : List<Matchup> {
        return db.matchupsDao().getSpecificChampionMatchups(champion.name).map {
            Matchup(
                orig = Champion(it.championName),
                enemy = Champion(it.championEnemy),
                role = it.role,
                difficulty = it.difficulty,
                numWins = it.numWins,
                numTotal = it.numTotal,
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
                numWins = matchup.numWins,
                numTotal = matchup.numTotal,
                description = matchup.description
            )
        )
    }

    override suspend fun deleteMatchups(champion : String, role : Role, matchups: List<Matchup>) {
        db.matchupsDao().deleteMatchups(
            champion,role, matchups.map {
                it.enemy.name
            }
        )
    }
}