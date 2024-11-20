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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.yellowtubby.victoryvault.ui.screens.Route
import com.yellowtubby.victoryvault.ui.screens.add.AddMatchupScreen
import com.yellowtubby.victoryvault.ui.screens.champion.MatchupScreen
import com.yellowtubby.victoryvault.ui.screens.matchup.MainScreenIntent
import com.yellowtubby.victoryvault.ui.screens.matchup.MainScreen
import com.yellowtubby.victoryvault.ui.screens.profile.ProfileScreen
import com.yellowtubby.victoryvault.ui.screens.statistics.StatisticsScreen
import com.yellowtubby.victoryvault.ui.screens.uicomponents.MatchBottomNavigation
import com.yellowtubby.victoryvault.ui.screens.uicomponents.MatchSnackBar
import com.yellowtubby.victoryvault.ui.screens.uicomponents.SnackBarType
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

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainContent() {
    val mainViewModel = koinViewModel<MatchupViewModel>()
    val snackBarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    LoadMainData(mainViewModel,snackBarState)
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
            snackbarHost = {
                SnackbarHost(snackBarState)
                { data ->
                    val parts = data.visuals.message.split(": ")
                    val title = parts[0]
                    val description = parts.getOrNull(1) ?: ""
                    val type = if (title == "Error") SnackBarType.ERROR else SnackBarType.SUCCESS
                    MatchSnackBar(title, description, type)
                }
            },
            floatingActionButton = {
                MatchFab(
                    mainViewModel = mainViewModel,
                    navController = navController,
                    scope = scope
                )
            }
        ) { innerPadding ->
            NavHost(navController = navController, startDestination = Route.Home.route) {
                composable(Route.Home.route) {
                    HandleBottomBarVisibility(mainViewModel, true)
                    MainScreen(
                        innerPadding = innerPadding,
                        scope = scope,
                        navController = navController,
                        mainViewModel = mainViewModel,
                        snackbarHostState = snackBarState
                    )
                }
                composable(Route.MatchupInfo.route) {
                    HandleBottomBarVisibility(mainViewModel, false)
                    MatchupScreen(
                        mainViewModel,
                        scope
                    )
                }

                composable(route = Route.AddMatchup.route) {
                    HandleBottomBarVisibility(mainViewModel, false)
                    AddMatchupScreen(
                        mainViewModel = mainViewModel,
                        scope = scope,
                        navController = navController
                    )
                }

                composable(route = Route.MyProfile.route) {
                    ProfileScreen()
                }

                composable(route = Route.Statistics.route) {
                    StatisticsScreen()
                }
            }
        }
        MatchupProgressIndicator(mainViewModel)
    }
}

@Composable
fun LoadMainData(mainViewModel: MatchupViewModel, snackbarHostState: SnackbarHostState) {
    LaunchedEffect(true) {
        mainViewModel.intentChannel.trySend(
            MainScreenIntent.LoadLocalData
        )
    }
}

@Composable
fun HandleBottomBarVisibility(mainViewModel: MatchupViewModel, shouldBeVisibile : Boolean) {
    LaunchedEffect(true) {
        mainViewModel.intentChannel.trySend(
            MainScreenIntent.ShowBottomBar(shouldBeVisibile)
        )
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
