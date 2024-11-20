package com.yellowtubby.victoryvault.ui.screens.uicomponents

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yellowtubby.victoryvault.ui.screens.MatchupViewModel
import com.yellowtubby.victoryvault.ui.screens.Route
import com.yellowtubby.victoryvault.ui.screens.add.AddChampionIntent
import com.yellowtubby.victoryvault.ui.screens.matchup.MainScreenIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi")
@Composable
fun MatchFab(
    scope : CoroutineScope,
    mainViewModel: MatchupViewModel,
    navController: NavController
) {
    val uiState by mainViewModel.uiStateMainActivity.collectAsState()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val expanded = remember { mutableStateOf(false) }

    if(currentBackStackEntry?.destination?.route == Route.Home.route){
        Column(horizontalAlignment = Alignment.End) {
            if (uiState.isFabExpanded) {
                ActionButtonWithLabel(
                    icon = Icons.Filled.Face,
                    label = "Add Matchup",
                    onClick = {
                        navController.navigate(Route.AddMatchup.route)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ActionButtonWithLabel(
                    icon = Icons.Filled.Face,
                    label = "Add Champion",
                    onClick = {
                        expanded.value = true
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                if(expanded.value){
                    ChampionDropdown(
                        mainViewModel.allChampions,
                        {
                            scope.launch {
                                mainViewModel.intentChannel.trySend(
                                    AddChampionIntent.AddChampion(
                                        champion = it
                                    )
                                )
                            }
                        },
                        expanded,
                        remember { mutableIntStateOf(0) },
                    )
                }
            }
            ExtendedFloatingActionButton(
                onClick = { scope.launch {
                    mainViewModel.intentChannel.trySend(
                        MainScreenIntent.FabExpanded(!uiState.isFabExpanded)
                    )
                } },
                icon = { Icon(Icons.Filled.Add, contentDescription = "Add") },
                text = { Text("Actions") }
            )
        }
    }
}