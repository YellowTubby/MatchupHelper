package com.yellowtubby.victoryvault.room

import androidx.room.TypeConverter
import com.yellowtubby.victoryvault.ui.model.DamageModifier
import com.yellowtubby.victoryvault.ui.model.DamageType
import com.yellowtubby.victoryvault.ui.model.Role


class MatchupTypeConverters() {
    @TypeConverter
    fun stringToRole(role: String): Role {
        return when(role){
            "Top" -> Role.TOP
            "Jungle" -> Role.JUNGLE
            "Mid" -> Role.MID
            "Bottom" -> Role.BOTTOM
            "Support" -> Role.SUPPORT
            else -> Role.TOP
        }
    }

    @TypeConverter
    fun roleToString(role: Role): String {
        return when(role){
            Role.TOP -> "Top"
            Role.JUNGLE -> "Jungle"
            Role.MID -> "Mid"
            Role.BOTTOM -> "Bottom"
            Role.SUPPORT -> "Support"
        }
    }

    @TypeConverter
    fun stringToDamageModifier(value: String): DamageModifier {
        value.split("_").also {
            return DamageModifier(stringToDamageType(it[0]), it[1].toInt())
        }
    }

    @TypeConverter
    fun damageModToString(value: DamageModifier): String {
        return value.type.name + "_" + value.percentage
    }

    private fun stringToDamageType(s: String): DamageType {
        return when(s){
            "AP" -> DamageType.AP
            "AD" -> DamageType.AD
            "HP" -> DamageType.HP
            "Armor" -> DamageType.Armor
            "MagicResist" -> DamageType.MagicResist
            else -> DamageType.AD
        }
    }
}
