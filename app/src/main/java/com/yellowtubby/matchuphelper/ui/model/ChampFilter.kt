package com.yellowtubby.matchuphelper.ui.model

data class ChampFilter(
    val filterFunction : (Champion) -> Boolean
)
