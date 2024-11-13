package com.yellowtubby.victoryvault.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DamageModifier(
    val type: DamageType,
    val percentage: Int
) : Parcelable

enum class DamageType {
    AD,
    AP,
    Armor,
    MagicResist,
    HP
}
