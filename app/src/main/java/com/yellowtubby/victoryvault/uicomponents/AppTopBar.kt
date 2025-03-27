package com.yellowtubby.victoryvault.uicomponents

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yellowtubby.victoryvault.R
import com.yellowtubby.victoryvault.core.presentation.ApplicationIntent
import com.yellowtubby.victoryvault.app.presentation.screen.BACK_BUTTON_STRING
import com.yellowtubby.victoryvault.app.presentation.screen.MENU_BUTTON_STRING
import com.yellowtubby.victoryvault.app.presentation.screen.MENU_DELETE_STRING
import com.yellowtubby.victoryvault.app.presentation.screen.MENU_EDIT_STRING
import com.yellowtubby.victoryvault.app.presentation.screen.MainActivityIntent
import com.yellowtubby.victoryvault.app.presentation.screen.MainActivityUIState
import com.yellowtubby.victoryvault.core.utils.Route
import com.yellowtubby.victoryvault.feature.main.presentation.screens.MainScreenIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun getIntentBasedOnNavController(actionString: String,
                                  navController: NavController,
                                  uiState: MainActivityUIState,
): ApplicationIntent {
    return when(navController.currentDestination?.route){
        Route.Home.route -> {
            when(actionString){
                MENU_DELETE_STRING -> {
                    if (uiState.multiSelectEnabled) {
                        MainScreenIntent.DeleteSelected(
                            uiState.selectedMatchups
                        )
                    } else {
                        ApplicationIntent()
                    }
                }
                else -> {
                    ApplicationIntent()
                }
            }
        }
        else -> {
            ApplicationIntent()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchTopBar(
    scope: CoroutineScope,
    uiState: MainActivityUIState,
    navController: NavController,
    activity: Activity? = LocalContext.current as Activity,
    onIntentEmitted: (ApplicationIntent) -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val leftActionList = listOf(BACK_BUTTON_STRING)
    val rightActionList = getActionsBasedOnStateAndDestination(uiState, navController)
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = if (uiState.multiSelectEnabled) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            if (uiState.multiSelectEnabled) {
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
                            if (uiState.multiSelectEnabled) {
                                scope.launch {
                                    onIntentEmitted(
                                        MainScreenIntent.StartMultiSelectChampion(false)
                                    )
                                }
                            } else {
                                scope.launch {
                                    onIntentEmitted(
                                        MainActivityIntent.NavigatedBottomBar(1)
                                    )
                                }
                                if(navController.currentDestination?.route == Route.Home.route || !navController.popBackStack()){
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
                val onClickCallback : () -> Unit = {
                    scope.launch {
                        onIntentEmitted(
                            getIntentBasedOnNavController(it, navController, uiState)
                        )
                    }
                }
                when (it) {
                    MENU_BUTTON_STRING -> {
                        IconButton(onClick = onClickCallback) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                    MENU_DELETE_STRING -> {
                        IconButton(onClick = onClickCallback) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                    MENU_EDIT_STRING -> {
                        IconButton(onClick = onClickCallback) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
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

fun getActionsBasedOnStateAndDestination(uiState : MainActivityUIState, navController: NavController): List<String> {
    return when(navController.currentDestination?.route){
        Route.Home.route -> {
            if (uiState.multiSelectEnabled) listOf(MENU_DELETE_STRING) else listOf(MENU_BUTTON_STRING)
        }

        Route.MatchupInfo.route -> {
            listOf(MENU_EDIT_STRING, MENU_BUTTON_STRING)
        }

        else -> {
            listOf(MENU_BUTTON_STRING)
        }
    }
}
