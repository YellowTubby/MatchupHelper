package com.yellowtubby.victoryvault.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yellowtubby.victoryvault.R
import com.yellowtubby.victoryvault.ui.uicomponents.ChampionSelector
import com.yellowtubby.victoryvault.ui.uicomponents.MatchupCard
import com.yellowtubby.victoryvault.ui.screens.getIconPainerResource
import com.yellowtubby.victoryvault.model.Role
import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.ui.screens.Route
import com.yellowtubby.victoryvault.ui.uicomponents.MatchupProgressIndicator
import com.yellowtubby.victoryvault.ui.uicomponents.SnackBarType
import com.yellowtubby.victoryvault.ui.uicomponents.SnackbarManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun MainScreen(
    navController: NavController,
    mainScreenViewModel: MainScreenViewModel,
    scope: CoroutineScope,
    innerPadding: PaddingValues,
    snackbarHostState: SnackbarHostState
) {
    val uiState: MainScreenUIState by mainScreenViewModel.uiState.collectAsState()
    MatchupProgressIndicator(uiState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (uiState.definedChampion.isNotEmpty()) Arrangement.Top else Arrangement.Center
        ) {
            LaunchedEffect(uiState.snackBarMessage) {
                scope.launch {
                    if (uiState.snackBarMessage.first) {
                        val showingSnackBar = async {
                            val snackbarManager = SnackbarManager(
                                snackbarHostState, scope
                            )
                            when (uiState.snackBarMessage.second.type) {
                                SnackBarType.SUCCESS -> snackbarManager.showSuccessSnackbar(
                                    uiState.snackBarMessage.second.description
                                )

                                SnackBarType.ERROR -> snackbarManager.showErrorSnackbar(
                                    uiState.snackBarMessage.second.description
                                )

                                SnackBarType.INFO -> snackbarManager.showInfoSnackbar(
                                    uiState.snackBarMessage.second.description
                                )
                            }
                        }
                        val clearingError = async {
                            mainScreenViewModel.emitIntent(
                                MainScreenIntent.ErrorClear
                            )
                        }
                        awaitAll(showingSnackBar, clearingError)
                    }
                }
            }
            if (uiState.currentChampion != Champion.NAN) {
                ChampionSelector(
                    uiState.definedChampion,
                    uiState.currentChampion
                ) { champion ->
                    scope.launch {
                        mainScreenViewModel.emitIntent(
                            MainScreenIntent.SelectChampion(champion)
                        )
                    }
                }
                RoleSegmentedButton(
                    uiState,
                    mainViewModel = mainScreenViewModel,
                    scope
                )
                Spacer(modifier = Modifier.size(8.dp))

                ChampionFilter(
                    scope,
                    mainScreenViewModel,
                )

                if (uiState.currentMatchupList.isNotEmpty()) {
                    MatchUpList(
                        mainScreenViewModel,
                        uiState,
                        navController,
                        scope
                    )
                } else {
                    NoMatchupSection(
                        navController
                    )
                }
            }
        }

        if (uiState.definedChampion.isEmpty()) {
            val selectedChampion = remember { mutableStateOf(uiState.currentChampion.name) }
            Text(
                modifier = Modifier.padding(16.dp),
                text = "You currently have no selected champions, please add a new champion",
                textAlign = TextAlign.Center
            )
            ChampionSelector(
                uiState.allChampions,
                Champion(selectedChampion.value)
            ) {
                selectedChampion.value = it.name
            }
            Spacer(modifier = Modifier.size(8.dp))
            Button(
                enabled = selectedChampion.value != "NAN",
                onClick = {
                    mainScreenViewModel.emitIntent(
                        MainScreenIntent.AddChampion(Champion(selectedChampion.value))
                    )
                }
            ) {
                Text(text = "Add Champion")
            }
        }
    }
}

@Composable
fun NoMatchupSection(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.no_matchups),
            textAlign = TextAlign.Center
        )
        Button(onClick = {
            navController.navigate("addMatchup")
        }) {
            Text(text = stringResource(R.string.add_matchup_string))
        }
    }
}

@Composable
fun MatchUpList(
    mainScreenViewModel: MainScreenViewModel,
    uiState: MainScreenUIState,
    navController: NavController,
    scope: CoroutineScope
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(8.dp),
        columns = GridCells.Fixed(3)
    ) {
        val stableMatchupList = uiState.currentMatchupList
        items(stableMatchupList) {
            Timber.d("MATCHUP SHOWING!: ${it}")
            MatchupCard(
                mainScreenViewModel, scope = scope, it
            ) {
                val selectedMatchup = it
                scope.launch {
                    if (uiState.multiSelectEnabled) {
                        Timber.d("EMITTING! to MULTISELECT ${selectedMatchup}")
                        mainScreenViewModel.emitIntent(
                            MainScreenIntent.MultiSelectMatchups(selectedMatchup)
                        )
                    } else {
                        mainScreenViewModel.emitIntent(
                            MainScreenIntent.SelectedMatchup(selectedMatchup)
                        )
                        navController.navigate(Route.MatchupInfo.route) {
                            popUpTo(Route.Home.route) {
                                inclusive = false
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChampionFilter(
    scope: CoroutineScope,
    mainViewModel: MainScreenViewModel
) {
    val uiState: MainScreenUIState by mainViewModel.uiState.collectAsState()
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(8.dp)),
        value = uiState.textQuery,
        onValueChange = { str: String ->
            scope.launch {
                mainViewModel.emitIntent(
                    MainScreenIntent.TextFilterChanged(filter = str)
                )
            }
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search"
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleSegmentedButton(
    uiState: MainScreenUIState,
    mainViewModel: MainScreenViewModel,
    scope: CoroutineScope
) {
    val options = listOf(Role.TOP, Role.JUNGLE, Role.MID, Role.BOTTOM, Role.SUPPORT)
    MultiChoiceSegmentedButtonRow {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                icon = {
                    SegmentedButtonDefaults.Icon(active = index == uiState.currentRole.ordinal) {
                        Icon(
                            painter = getIconPainerResource(options[index]),
                            contentDescription = null,
                            modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                        )
                    }
                },
                onCheckedChange = {
                    scope.launch {
                        mainViewModel.emitIntent(
                            MainScreenIntent.RoleChanged(options[index])
                        )
                    }
                },
                checked = uiState.currentRole?.ordinal == index
            ) {
                Text(
                    text =
                    when (options[index]) {
                        Role.TOP -> "Top"
                        Role.JUNGLE -> "Jungle"
                        Role.MID -> "Mid"
                        Role.BOTTOM -> "Bottom"
                        Role.SUPPORT -> "Support"
                        Role.NAN -> "NAN"
                    }
                )
            }
        }
    }
}
