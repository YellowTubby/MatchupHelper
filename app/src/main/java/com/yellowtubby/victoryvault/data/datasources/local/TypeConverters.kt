package com.yellowtubby.victoryvault.data.datasources.local

import androidx.room.TypeConverter
import com.yellowtubby.victoryvault.data.datamodels.Role


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
            Role.NAN -> "NAN"
        }
    }

}
