package com.yellowtubby.matchuphelper.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.yellowtubby.matchuphelper.R
import com.yellowtubby.matchuphelper.ui.model.Champion
import com.yellowtubby.matchuphelper.ui.screens.matchup.MatchupIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchTopBar(
    scope: CoroutineScope,
    mainViewModel: MatchupViewModel,
    navController: NavController
) {
    val uiState by mainViewModel.uiStateAppBar.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val leftActionList = listOf(BACK_BUTTON_STRING)
    val rightActionList = if(uiState.isInMultiSelect) listOf(MENU_DELETE_STRING) else listOf(MENU_BUTTON_STRING)
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = if(uiState.isInMultiSelect) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            if(uiState.isInMultiSelect){
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = "${uiState.selectedAmount} selected"
                )
            } else {
                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(48.dp),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Application Logo"
                )
            }
        },
        navigationIcon = {
            leftActionList.forEach {
                when (it) {
                    BACK_BUTTON_STRING -> {
                        IconButton(onClick = {
                            if(uiState.isInMultiSelect){
                                scope.launch {
                                    mainViewModel.intentChannel.trySend(
                                        MatchupIntent.StartMultiSelectChampion(false)
                                    )
                                }
                            } else {
                                navController.popBackStack()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    }

                    else -> Unit
                }
            }
        },
        actions = {
            rightActionList.forEach {
                when (it) {
                    MENU_BUTTON_STRING -> {
                        IconButton(onClick = { /* do something */ }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                    MENU_DELETE_STRING -> {
                        IconButton(onClick = {
                            if(uiState.isInMultiSelect){
                                scope.launch {
                                    mainViewModel.intentChannel.trySend(
                                        MatchupIntent.DeleteSelected
                                    )
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun ChampionSelector(
    championList: List<Champion>
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
        ChampionListItem(
            championList[selectedIndex]
        ) {
            expanded = true
        }
        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            championList.forEachIndexed { index, champ ->
                DropdownMenuItem(
                    text = { Text(text = champ.name) }, onClick = {
                        selectedIndex = index
                        expanded = false
                    })
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChampionListItem(champion: Champion, onItemClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable(onClick = onItemClicked),
        horizontalArrangement = Arrangement.Absolute.Left,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            modifier = Modifier
                .size(48.dp)
                .padding(8.dp)
                .clip(CircleShape),

            model = champion.iconUri, contentDescription = "icon_champion_${champion.name}"
        )
        Text(
            champion.name,
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
        )
        Spacer(Modifier.weight(1f))
        Icon(
            modifier = Modifier,
            imageVector = Icons.Filled.ArrowDropDown, contentDescription = "DropDown Arrow"
        )
    }

}

@Composable
fun MatchFab(
    mainViewModel: MatchupViewModel,
    navController: NavController
) {
    FloatingActionButton(
        onClick = { navController.navigate("addMatchup/{currentChampion}") },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
    }
}