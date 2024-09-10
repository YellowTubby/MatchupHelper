package com.yellowtubby.matchuphelper.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.yellowtubby.matchuphelper.ui.model.Ability
import com.yellowtubby.matchuphelper.ui.model.DamageModifier
import com.yellowtubby.matchuphelper.ui.model.Matchup

@Entity
data class ChampionEntity(
    @PrimaryKey val champion_name: String,
)

const val LIST_DELIMITER = "@"

@Entity
data class AbilityEntity(
    @PrimaryKey val ability_id: String,
    @ColumnInfo(name = "champion_name") val championName: String,
    @ColumnInfo(name = "ability_name") val abilityName: String,
    @ColumnInfo(name = "ability_type") val abilityType: String,
    @ColumnInfo(name = "cooldown_list") val cooldowns: String,
    @ColumnInfo(name = "damage_or_shield_list") val damageOrShieldList: String,
    @ColumnInfo(name = "modifier") val modifier: DamageModifier
)

data class ChampionAbilities(
    @Embedded val champion: ChampionEntity,
    @Relation(
        parentColumn = "champion_name",
        entityColumn = "champion_name"
    )
    val abilities: List<AbilityEntity>
)
