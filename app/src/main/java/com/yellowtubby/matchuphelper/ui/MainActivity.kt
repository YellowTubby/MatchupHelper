package com.yellowtubby.matchuphelper.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yellowtubby.matchuphelper.ui.screens.MainActivityUIState
import com.yellowtubby.matchuphelper.ui.screens.MatchFab
import com.yellowtubby.matchuphelper.ui.screens.MatchTopBar
import com.yellowtubby.matchuphelper.ui.screens.MatchupViewModel
import com.yellowtubby.matchuphelper.ui.screens.add.AddChampionScreen
import com.yellowtubby.matchuphelper.ui.screens.add.AddMatchupScreen
import com.yellowtubby.matchuphelper.ui.screens.champion.ChampionScreen
import com.yellowtubby.matchuphelper.ui.screens.matchup.MatchupIntent
import com.yellowtubby.matchuphelper.ui.screens.matchup.MatchupScreen
import com.yellowtubby.matchuphelper.ui.screens.matchup.MatchupUiState
import com.yellowtubby.matchuphelper.ui.theme.MatchupHelperTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val mainViewModel = koinViewModel<MatchupViewModel>()
            val scope = rememberCoroutineScope()
            LaunchedEffect(true) {
                scope.launch {
                    mainViewModel.intentChannel.trySend(
                        MatchupIntent.LoadLocalData
                    )
                }
            }
            MatchupHelperTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        MatchTopBar(
                            scope = scope,
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
                ) {
                    innerPadding ->
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            MatchupScreen(
                                innerPadding = innerPadding,
                                scope = scope,
                                navController = navController,
                                mainViewModel = mainViewModel
                            )
                        }
                        composable(
                            "champion/{championID}",
                            arguments = listOf(navArgument("championID") {
                                type = NavType.StringType
                            })
                        ) { backstackEntry ->
                            ChampionScreen(
                                backstackEntry.arguments?.getString("championID")
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
                    }
                }
                MatchupProgressIndicator(mainViewModel)
            }
        }
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
