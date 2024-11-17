package com.yellowtubby.victoryvault.ui.screens.matchup

import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.yellowtubby.victoryvault.R
import com.yellowtubby.victoryvault.ui.screens.uicomponents.ChampionSelector
import com.yellowtubby.victoryvault.ui.screens.MatchupViewModel
import com.yellowtubby.victoryvault.ui.screens.uicomponents.MatchupCard
import com.yellowtubby.victoryvault.ui.screens.getIconPainerResource
import com.yellowtubby.victoryvault.ui.model.Role
import com.yellowtubby.victoryvault.ui.screens.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

@Composable
fun MainScreen(
    navController: NavController,
    scope: CoroutineScope,
    innerPadding: PaddingValues,
    mainViewModel: MatchupViewModel
) {
    val uiState: MainScreenUiState by mainViewModel.uiStateMainScreen.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if(uiState.definedChampion.isNotEmpty()) Arrangement.Top else Arrangement.Center
    ) {
        uiState.currentChampion?.let {
            ChampionSelector(
                uiState.definedChampion,
                it
            ) {
                champion ->
                scope.launch {
                    mainViewModel.intentChannel.trySend(
                        MainScreenIntent.SelectChampion(champion)
                    )
                }
            }
            RoleSegmentedButton(
                uiState,
                mainViewModel = mainViewModel,
                scope
            )
            Spacer(modifier = Modifier.size(8.dp))

            ChampionFilter(
                scope,
                mainViewModel,
            )
            var filteredList = uiState.matchupsForCurrentChampion.sortedBy { it.enemy.name }
            uiState.filterList.forEach {
                    filter ->
                        filteredList = filteredList.filter(filter.filterFunction)
            }
            filteredList = filteredList.filter {
                it.enemy.name.lowercase().contains(uiState.textQuery.lowercase())
            }
            if(filteredList.isNotEmpty()) {
                LazyVerticalGrid(
                    modifier = Modifier.padding(8.dp),
                    columns = GridCells.Fixed(3)
                ) {
                    items(filteredList) {
                        MatchupCard(
                            mainViewModel, scope = scope, it, it.difficulty
                        ) {
                            if (uiState.isInMultiSelect) {
                                scope.launch {
                                    mainViewModel.intentChannel.trySend(
                                        MainScreenIntent.MultiSelectMatchups(it)
                                    )
                                }
                            } else {
                                scope.launch {
                                    mainViewModel.intentChannel.trySend(
                                        MainScreenIntent.LoadMatchupInfo(it)
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
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
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
        }
        if(uiState.currentChampion == null){
            Text(
                modifier = Modifier.padding(16.dp),
                text = "You currently have no selected champions, please add a new champion",
                textAlign = TextAlign.Center

            )
            Spacer(modifier = Modifier.size(8.dp))
            Button(
                onClick = {
                    navController.navigate("addChampion")
                }
            ) {
                Text(text = "Add Champion")
            }
        }


    }
}

@Composable
fun ChampionFilter(
    scope: CoroutineScope,
    mainViewModel: MatchupViewModel
) {
    val uiState: MainScreenUiState by mainViewModel.uiStateMainScreen.collectAsState()
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(8.dp)),
        value = uiState.textQuery,
        onValueChange = { str: String ->
            scope.launch {
                mainViewModel.intentChannel.trySend(
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
    uiState: MainScreenUiState,
    mainViewModel: MatchupViewModel,
    scope: CoroutineScope
) {
    val options = listOf(Role.TOP, Role.JUNGLE, Role.MID, Role.BOTTOM, Role.SUPPORT)
    MultiChoiceSegmentedButtonRow {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                icon = {
                    SegmentedButtonDefaults.Icon(active = index == uiState.currentRole?.ordinal) {
                        Icon(
                            painter = getIconPainerResource(options[index]),
                            contentDescription = null,
                            modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                        )
                    }
                },
                onCheckedChange = {
                    scope.launch {
                        mainViewModel.intentChannel.trySend(
                            MainScreenIntent.RoleChanged(options[index])
                        )
                    }
                },
                checked = uiState.currentRole?.ordinal == index
            ) {
                Text(text =
                    when(options[index]){
                        Role.TOP -> "Top"
                        Role.JUNGLE -> "Jungle"
                        Role.MID -> "Mid"
                        Role.BOTTOM -> "Bottom"
                        Role.SUPPORT -> "Support"
                })
            }
        }
    }
}
