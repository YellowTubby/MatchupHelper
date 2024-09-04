package com.yellowtubby.matchuphelper.room

import androidx.room.TypeConverter
import com.yellowtubby.matchuphelper.ui.model.Role


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
}
