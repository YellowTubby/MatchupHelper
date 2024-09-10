package com.yellowtubby.matchuphelper.ui.model

data class Ability(
    val championName : String,
    val iconUri: String,
    val type: AbilityType,
    val cooldownList: List<String>,
    val damageOrShieldList : List<String>,
    val modifier: DamageModifier
)

enum class AbilityType {
    P,
    Q,
    W,
    R,
    E
}