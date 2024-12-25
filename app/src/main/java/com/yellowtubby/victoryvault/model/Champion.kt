package com.yellowtubby.victoryvault.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Champion(val name: String, val iconUri: String, val splashUri: String) : Parcelable {
    constructor(name: String = "NAN") : this(
        name = name,
        iconUri = "https://ddragon.leagueoflegends.com/cdn/14.17.1/img/champion/$name.png",
        splashUri = "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/$name" +"_0.jpg",
    )

}