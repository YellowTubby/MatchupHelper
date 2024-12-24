package com.yellowtubby.victoryvault.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ability(
    val abilityName : String,
    val championName : String,
    val iconUri: String,
    val type: AbilityType,
    val cooldownList: List<String>,
    val damageOrShieldList : List<String>,
    val modifier: DamageModifier
) : Parcelable

enum class AbilityType {
    P,
    Q,
    W,
    R,
    E
}