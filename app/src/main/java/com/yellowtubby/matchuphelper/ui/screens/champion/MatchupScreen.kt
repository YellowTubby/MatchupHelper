package com.yellowtubby.matchuphelper.ui.screens.champion

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yellowtubby.matchuphelper.ui.model.Matchup
import com.yellowtubby.matchuphelper.ui.screens.MatchupViewModel


@Composable
fun MatchupScreen(
    mainViewModel : MatchupViewModel,
    matchup: Matchup
) {
    val uiState by mainViewModel.uiStateMatchupScreen.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "NAME: ${matchup.orig.name} vs ENEMY: ${matchup.enemy.name}, DIFFICULTY: ${matchup.difficulty}, ROLE: ${matchup.role} , DESCRIPTION: ${matchup.description}")
    }
}