package com.yellowtubby.victoryvault.data.datamodels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Matchup(
    val orig : Champion = Champion("Aatrox"),
    val enemy : Champion = Champion("Aatrox"),
    val role : Role = Role.NAN,
    val description: String = "",
    val numWins: Int = 0,
    val numTotal: Int = 0,
    val difficulty: Int = 0
) : Parcelable {
    override fun toString(): String {
        return "Matchup(${orig.name} -> ${enemy.name})"
    }
}

