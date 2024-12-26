package com.yellowtubby.victoryvault.ui.uicomponents

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.yellowtubby.victoryvault.R
import com.yellowtubby.victoryvault.ui.MainActivityIntent
import com.yellowtubby.victoryvault.ui.MainActivityViewModel
import com.yellowtubby.victoryvault.ui.screens.matchup.MatchupViewModel
import com.yellowtubby.victoryvault.ui.screens.Route
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenIntent

data class BottomNavItem(val route: String,
                         val iconSelected: @Composable () -> Unit,
                         val iconUnselected: @Composable () -> Unit,
                         val label: String)

internal val navList = listOf(
    BottomNavItem(Route.MyProfile.route,
        { Icon(Icons.Filled.Person, "homeBtn_selected") },
        { Icon(Icons.Outlined.Person, "homeBtn_unselected") },
        "Profile"),

    BottomNavItem(
        Route.Home.route,
        @Composable { Icon(painter = painterResource(R.drawable.sword_cross), "matchup_BtnSelected") },
        @Composable { Icon(painter = painterResource(R.drawable.sword_cross_outlined), "matchup_BtnUnSelected") },
        "Matchups"),

    BottomNavItem(Route.Statistics.route,
        @Composable { Icon(painter = painterResource(R.drawable.poll), "statistics selected") },
        @Composable { Icon(painter = painterResource(R.drawable.poll_outlined), "statistics not selected") },
        "Statistics")
)

@Composable
fun MatchBottomNavigation(
    navController: NavController,
    mainViewModel: MainActivityViewModel
) {
    TabView(navList,navController,mainViewModel)
}


// ----------------------------------------
// This is a wrapper view that allows us to easily and cleanly
// reuse this component in any future project
@Composable
fun TabView(
    tabBarItems: List<BottomNavItem>,
    navController: NavController,
    viewModel: MainActivityViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    if(uiState.isBottomBarVisible){
        NavigationBar {
            // looping over each tab to generate the views and navigation for each item
            tabBarItems.forEachIndexed { index, tabBarItem ->
                NavigationBarItem(
                    selected = uiState.selectedBottomBarIndex == index,
                    onClick = {
                        navController.navigate(tabBarItem.route) {
                            popUpToRoute?.let {
                                popUpTo(it) {
                                    inclusive = true
                                }
                            }
                        }
                        viewModel.emitIntent(
                            MainActivityIntent.NavigatedBottomBar(index)
                        )
                    },
                    icon = {
                        TabBarIconView(
                            isSelected = uiState.selectedBottomBarIndex == index,
                            selectedIcon = tabBarItem.iconSelected,
                            unselectedIcon = tabBarItem.iconUnselected,
                        )
                    },
                    label = {Text(tabBarItem.label)})
            }
        }
    }
}

// This component helps to clean up the API call from our TabView above,
// but could just as easily be added inside the TabView without creating this custom component
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: @Composable () -> Unit,
    unselectedIcon: @Composable () -> Unit,
    badgeAmount: Int? = null
) {
    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        if(isSelected){
            selectedIcon()
        } else {
            unselectedIcon()
        }
    }
}

// This component helps to clean up the API call from our TabBarIconView above,
// but could just as easily be added inside the TabBarIconView without creating this custom component
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}