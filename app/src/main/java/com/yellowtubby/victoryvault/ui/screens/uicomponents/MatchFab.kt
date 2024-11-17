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
import com.yellowtubby.victoryvault.ui.screens.matchup.MainScreenUiState

@SuppressLint("RestrictedApi")
@Composable
fun MatchFab(
    mainViewModel: MatchupViewModel,
    navController: NavController
) {
    val uiState : MainScreenUiState by mainViewModel.uiStateMainScreen.collectAsState()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    var fabExpanded by remember { mutableStateOf(false) }
    if(currentBackStackEntry?.destination?.route == Route.Home.route){
        Column(horizontalAlignment = Alignment.End) {
            if (fabExpanded) {
                ActionButtonWithLabel(
                    icon = Icons.Filled.Face,
                    label = "Add Matchup",
                    onClick = {
                        navController.navigate("addMatchup")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ActionButtonWithLabel(
                    icon = Icons.Filled.Face,
                    label = "Add Champion",
                    onClick = {
                        navController.navigate("addChampion")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            ExtendedFloatingActionButton(
                onClick = { fabExpanded = !fabExpanded },
                icon = { Icon(Icons.Filled.Add, contentDescription = "Add") },
                text = { Text("Actions") }
            )
        }
    }
}