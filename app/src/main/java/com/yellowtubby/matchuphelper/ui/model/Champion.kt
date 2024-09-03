package com.yellowtubby.matchuphelper.ui.model

data class Champion(val name: String, val iconUri : String) {
    constructor(name: String) : this(name, "https://ddragon.leagueoflegends.com/cdn/14.17.1/img/champion/$name.png")
}