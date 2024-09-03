package com.yellowtubby.matchuphelper.ui.screens.matchup

import com.yellowtubby.matchuphelper.ui.model.ChampFilter
import com.yellowtubby.matchuphelper.ui.model.Champion

val INIT_STATE = MatchupUiState(
    definedChampion = listOf(
        Champion("Akali"),
        Champion("Ziggs"),
        Champion("Ahri"),
        Champion("Mordekaiser"),
        Champion("Ekko"),
    ),
    currentChampion = Champion("Akali"),
    matchupsForCurrentChampion = listOf(
        Champion("Akali"),
        Champion("Ziggs"),
        Champion("Ahri"),
        Champion("Mordekaiser"),
        Champion("Ekko"),
        Champion("Ezreal"),
        Champion("Ivern"),
    ),
    isInMultiSelect = false,
    selectedChampions = emptyList(),
    filterList = emptyList()
)
data class MatchupUiState(
    val definedChampion: List<Champion>,
    val currentChampion: Champion,
    val matchupsForCurrentChampion: List<Champion>,
    val isInMultiSelect : Boolean,
    val selectedChampions: List<Champion>,
    val filterList : List<ChampFilter>,
    val textQuery : String = ""
)

