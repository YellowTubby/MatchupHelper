package com.yellowtubby.victoryvault.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.VictoryVaultTheme
import com.yellowtubby.victoryvault.ui.screens.MainActivityUIState
import com.yellowtubby.victoryvault.ui.screens.uicomponents.MatchFab
import com.yellowtubby.victoryvault.ui.screens.uicomponents.MatchTopBar
import com.yellowtubby.victoryvault.ui.screens.MatchupViewModel
import com.yellowtubby.victoryvault.ui.screens.add.AddChampionScreen
import com.yellowtubby.victoryvault.ui.screens.add.AddMatchupScreen
import com.yellowtubby.victoryvault.ui.screens.champion.MatchupScreen
import com.yellowtubby.victoryvault.ui.screens.matchup.MainScreenIntent
import com.yellowtubby.victoryvault.ui.screens.matchup.MainScreen
import com.yellowtubby.victoryvault.ui.screens.profile.ProfileScreen
import com.yellowtubby.victoryvault.ui.screens.statistics.StatisticsScreen
import com.yellowtubby.victoryvault.ui.screens.uicomponents.MatchBottomNavigation
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainContent()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainContent() {
    val mainViewModel = koinViewModel<MatchupViewModel>()
    val scope = rememberCoroutineScope()
    LaunchedEffect(true) {
        scope.launch {
            mainViewModel.intentChannel.trySend(
                MainScreenIntent.LoadLocalData
            )
        }
    }
    VictoryVaultTheme {
        val navController = rememberNavController()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                MatchTopBar(
                    scope = scope,
                    mainViewModel = mainViewModel,
                    navController = navController,
                )
            },
            bottomBar = {
                MatchBottomNavigation(
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            },
            floatingActionButton = {
                MatchFab(
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
        ) { innerPadding ->
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    MainScreen(
                        innerPadding = innerPadding,
                        scope = scope,
                        navController = navController,
                        mainViewModel = mainViewModel
                    )
                }
                composable(
                    "matchupInfo",
                ) {
                    MatchupScreen(
                        mainViewModel,
                        scope
                    )
                }

                composable(
                    route = "addMatchup",
                ) {
                    AddMatchupScreen(
                        mainViewModel = mainViewModel,
                        scope = scope,
                        navController = navController
                    )
                }

                composable(
                    route = "addChampion",
                ) {
                    AddChampionScreen(
                        mainViewModel = mainViewModel,
                        navController = navController,
                        scope = scope
                    )
                }
                composable(
                    route = "profile",
                ) {
                    ProfileScreen()
                }
                composable(
                    route = "statistics",
                ) {
                    StatisticsScreen()
                }
            }
        }
        MatchupProgressIndicator(mainViewModel)
    }
}

@Composable
fun MatchupProgressIndicator(
    mainViewModel: MatchupViewModel
) {
    val uiState: MainActivityUIState by mainViewModel.uiStateMainActivity.collectAsState()
    if (uiState.loading) {
        CircularProgressIndicator(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {}
                )
                .wrapContentSize(Alignment.Center)
                .size(80.dp)
        )
    }
}
