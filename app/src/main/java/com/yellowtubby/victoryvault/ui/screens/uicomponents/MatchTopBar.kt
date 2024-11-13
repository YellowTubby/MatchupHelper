package com.yellowtubby.victoryvault.ui.screens.uicomponents

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yellowtubby.victoryvault.R
import com.yellowtubby.victoryvault.ui.screens.BACK_BUTTON_STRING
import com.yellowtubby.victoryvault.ui.screens.MENU_BUTTON_STRING
import com.yellowtubby.victoryvault.ui.screens.MENU_DELETE_STRING
import com.yellowtubby.victoryvault.ui.screens.MatchupViewModel
import com.yellowtubby.victoryvault.ui.screens.matchup.MainScreenIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchTopBar(
    scope: CoroutineScope,
    mainViewModel: MatchupViewModel,
    navController: NavController,
    activity: Activity? = LocalContext.current as Activity
) {
    val uiState by mainViewModel.uiStateMainActivity.collectAsState()
    val uiStateForSelectedMatchups = mainViewModel.uiStateMainScreen.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val leftActionList = listOf(BACK_BUTTON_STRING)
    val rightActionList =
        if (uiState.isInMultiSelect) listOf(MENU_DELETE_STRING) else listOf(MENU_BUTTON_STRING)
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = if (uiState.isInMultiSelect) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            if (uiState.isInMultiSelect) {
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
                            if (uiState.isInMultiSelect) {
                                scope.launch {
                                    mainViewModel.intentChannel.trySend(
                                        MainScreenIntent.StartMultiSelectChampion(false)
                                    )
                                }
                            } else {
                                scope.launch {
                                    mainViewModel.intentChannel.trySend(
                                        MainScreenIntent.NavigatedBottomBar(1)
                                    )
                                }
                                if(navController.currentDestination?.route == "home" || !navController.popBackStack()){
                                    activity?.finish()
                                }
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
                            if (uiState.isInMultiSelect) {
                                scope.launch {
                                    mainViewModel.intentChannel.trySend(
                                        MainScreenIntent.DeleteSelected(
                                            uiStateForSelectedMatchups.value.selectedMatchups
                                        )
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