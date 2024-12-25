package com.yellowtubby.victoryvault.model

enum class Role {
    TOP,
    JUNGLE,
    MID,
    BOTTOM,
    SUPPORT,
    NAN
}

val roleToStringMap = mapOf(
    Role.TOP to "Top",
    Role.JUNGLE to "Jungle",
    Role.MID to "Mid",
    Role.BOTTOM to "Bottom",
    Role.SUPPORT to "Support"
)

val stringToRoleMap = roleToStringMap.map { (k,v) -> v to k }.toMap()