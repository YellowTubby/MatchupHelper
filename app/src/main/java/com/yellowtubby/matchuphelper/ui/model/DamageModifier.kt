package com.yellowtubby.matchuphelper.ui.model

data class DamageModifier(
    val type: DamageType,
    val percentage: Int
)

enum class DamageType {
    AD,
    AP,
    Armor,
    MagicResist
}
