package com.yellowtubby.matchuphelper.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yellowtubby.matchuphelper.ui.screens.add.AddScreen
import com.yellowtubby.matchuphelper.ui.screens.champion.ChampionScreen
import com.yellowtubby.matchuphelper.ui.screens.matchup.MatchupScreen
import com.yellowtubby.matchuphelper.ui.theme.MatchupHelperTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel = koinViewModel<MatchupViewModel>()
            val scope = rememberCoroutineScope()
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
                            mainViewModel,
                            navController
                        )
                    }
                ) { innerPadding ->
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
                            route = "addMatchup/{currentChampion}",
                            arguments = listOf(
                                navArgument("currentChampion") {
                                    type = NavType.StringType
                                },
                            ),
                        ) { backstackEntry ->
                            AddScreen(
                                backstackEntry.arguments?.getString("currentChampion")
                            )
                        }
                    }
                }
            }
        }
    }
}
