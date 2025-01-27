package com.yellowtubby.victoryvault.repositories

import android.util.Log
import com.yellowtubby.victoryvault.repositories.room.AbilityEntity
import com.yellowtubby.victoryvault.repositories.room.ChampionEntity
import com.yellowtubby.victoryvault.repositories.room.LIST_DELIMITER
import com.yellowtubby.victoryvault.repositories.room.MatchupDatabase
import com.yellowtubby.victoryvault.repositories.room.MatchupEntity
import com.yellowtubby.victoryvault.model.Ability
import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.model.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent.inject

class MatchupRepositoryImpl : MatchupRepository {
    val db: MatchupDatabase by inject(MatchupDatabase::class.java)
    val definedChampionFlow : MutableStateFlow<List<Champion>> = MutableStateFlow(emptyList())
    val matchupFlow : MutableStateFlow<List<Matchup>> = MutableStateFlow(emptyList())

    override suspend fun addChampion(champion: Champion) {
        db.matchupsDao().insertChampion(
            ChampionEntity(
                champion.name,
            )
        )
    }

    override suspend fun updateChampion(champion: Champion) {
        definedChampionFlow.value += champion
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

    override suspend fun updateMatchup(prevMatch: Matchup) {
        db.matchupsDao().updateMatchup(
            MatchupEntity(
                prevMatch.orig.name,
                prevMatch.enemy.name,
                prevMatch.role,
                prevMatch.numWins,
                prevMatch.numTotal,
                prevMatch.description,
                prevMatch.difficulty
            )
        )
        getAllMatchups()
    }

    override suspend fun getAllDefinedChampions(): Flow<List<Champion>> {
        definedChampionFlow.value = db.matchupsDao().getAllChampions().map {
            Champion(it.champion_name)
        }
        return definedChampionFlow.asStateFlow()
    }

    override suspend fun getAllMatchups(): Flow<List<Matchup>> {
        matchupFlow.value = db.matchupsDao().getAllMatchups().map {
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
        return matchupFlow.asStateFlow()
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
        getAllMatchups()
    }

    override suspend fun deleteMatchups(champion : String, role : Role, matchups: List<Matchup>) {
        db.matchupsDao().deleteMatchups(
            champion,role, matchups.map {
                it.enemy.name
            }
        )
        getAllMatchups()
    }
}