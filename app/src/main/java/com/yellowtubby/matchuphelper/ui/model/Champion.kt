package com.yellowtubby.matchuphelper.ui.model

data class Champion(val name: String, val iconUri: String, val splashUri: String) {
    constructor(name: String) : this(
        name = name,
        iconUri = "https://ddragon.leagueoflegends.com/cdn/14.17.1/img/champion/$name.png",
        splashUri = "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/$name" +"_0.jpg",
    )

}