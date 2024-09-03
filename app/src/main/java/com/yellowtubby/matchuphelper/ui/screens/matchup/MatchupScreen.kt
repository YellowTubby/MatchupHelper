package com.yellowtubby.matchuphelper.ui.screens.matchup

import android.widget.EditText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yellowtubby.matchuphelper.ui.ChampionSelector
import com.yellowtubby.matchuphelper.ui.MatchupViewModel
import com.yellowtubby.matchuphelper.ui.components.ChampionCard
import com.yellowtubby.matchuphelper.ui.model.ChampFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchupScreen(
    navController: NavController,
    scope: CoroutineScope,
    innerPadding: PaddingValues,
    mainViewModel: MatchupViewModel
) {
    val uiState: MatchupUiState by mainViewModel.uiStateMatchupScreen.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        ChampionSelector(
            uiState.definedChampion
        )
        ChampionFilter(
            scope,
            mainViewModel,
        )
        LazyVerticalGrid(
            modifier = Modifier.padding(8.dp),
            columns = GridCells.Fixed(3)
        ) {
            var filteredList = uiState.matchupsForCurrentChampion.sortedBy { it.name }
            uiState.filterList.forEach {
                filter -> filteredList = filteredList.filter(filter.filterFunction)
            }
            filteredList = filteredList.filter {
                it.name.lowercase().contains(uiState.textQuery.lowercase())
            }
            items(filteredList) {
                ChampionCard(
                    mainViewModel, scope = scope, it
                ) {
                    if (uiState.isInMultiSelect) {
                        scope.launch {
                            mainViewModel.intentChannel.trySend(
                                MatchupIntent.SelectChampion(it)
                            )
                        }
                    } else {
                        navController.navigate("champion/{${it.name}}")
                    }
                }
            }
        }

    }
}

@Composable
fun ChampionFilter(
    scope: CoroutineScope,
    mainViewModel: MatchupViewModel
) {
    val uiState: MatchupUiState by mainViewModel.uiStateMatchupScreen.collectAsState()
    TextField(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        value = uiState.textQuery,
        onValueChange = { str: String ->
            scope.launch {
                mainViewModel.intentChannel.trySend(
                    MatchupIntent.TextFilterChanged(filter = str)
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
