package com.yellowtubby.matchuphelper.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.yellowtubby.matchuphelper.R
import com.yellowtubby.matchuphelper.ui.model.Champion
import com.yellowtubby.matchuphelper.ui.model.Role
import com.yellowtubby.matchuphelper.ui.model.roleToStringMap
import com.yellowtubby.matchuphelper.ui.screens.matchup.MatchupIntent
import com.yellowtubby.matchuphelper.ui.screens.matchup.MatchupUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchTopBar(
    scope: CoroutineScope,
    mainViewModel: MatchupViewModel,
    navController: NavController
) {
    val uiState by mainViewModel.uiStateMainActivity.collectAsState()
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
                            if (uiState.isInMultiSelect) {
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChampionSelector(
    championList: List<Champion>,
    initialChampion: Champion = Champion("Aatrox"),
    onSelected: (Champion) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(championList.indexOf(initialChampion)) }
    Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
        if(championList.isNotEmpty()){
            ChampionListItem(
                championList[selectedIndex]
            ) {
                expanded = true
            }
        }
        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            championList.forEachIndexed { index, champ ->
                val inlineContent = mapOf(
                    Pair(
                        // This tells the [CoreText] to replace the placeholder string "[icon]" by
                        // the composable given in the [InlineTextContent] object.
                        champ.name,
                        InlineTextContent(
                            // Placeholder tells text layout the expected size and vertical alignment of
                            // children composable.
                            Placeholder(
                                width = 14.sp,
                                height = 14.sp,
                                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                            )
                        ) {
                            // This Icon will fill maximum size, which is specified by the [Placeholder]
                            // above. Notice the width and height in [Placeholder] are specified in TextUnit,
                            // and are converted into pixel by text layout.
                            GlideImage(
                                model = champ.iconUri, contentDescription = "icon_champion_dropdown${champ.name}"
                            )
                        }
                    )
                )
                DropdownMenuItem(
                    text = {
                            Text(
                                text =  buildAnnotatedString {
                                    append(champ.name)
                                    append("  ")
                                    appendInlineContent(champ.name)
                                },
                                inlineContent = inlineContent,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .wrapContentHeight(),

                            )
                    },
                    onClick = {
                        selectedIndex = index
                        onSelected(championList[selectedIndex])
                        expanded = false
                    }
                )
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

@SuppressLint("RestrictedApi")
@Composable
fun MatchFab(
    mainViewModel: MatchupViewModel,
    navController: NavController
) {
    val uiState :MatchupUiState by mainViewModel.uiStateMatchupScreen.collectAsState()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    var fabExpanded by remember { mutableStateOf(false) }
    if(currentBackStackEntry?.destination?.route == "home"){
        Column(horizontalAlignment = Alignment.End) {
            if (fabExpanded) {
                ActionButtonWithLabel(
                    icon = Icons.Filled.Face,
                    label = "Add Matchup",
                    onClick = {
                        navController.navigate("addMatchup/${uiState.currentChampion?.name}/${roleToStringMap[uiState.currentRole]}")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ActionButtonWithLabel(
                    icon = Icons.Filled.Face,
                    label = "Add Champion",
                    onClick = {
                        navController.navigate("addMatchup/${uiState.currentChampion?.name}/${roleToStringMap[uiState.currentRole]}")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            ExtendedFloatingActionButton(
                onClick = { fabExpanded = !fabExpanded },
                icon = { Icon(Icons.Filled.Add, contentDescription = "Add") },
                text = { Text("Actions") }
            )
        }
    }
}

@Composable
fun getIconPainerResource(role: Role?) : Painter {
    return when (role) {
        Role.TOP -> painterResource(R.drawable.top_icon)
        Role.JUNGLE -> painterResource(R.drawable.jungle_icon)
        Role.MID -> painterResource(R.drawable.mid_icon)
        Role.BOTTOM -> painterResource(R.drawable.bot_icon)
        Role.SUPPORT -> painterResource(R.drawable.support_icon)
        null -> painterResource(R.drawable.logo)
    }
}


@Composable
fun ActionButtonWithLabel(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .width(120.dp)
                .background(Color.Gray, shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        FloatingActionButton(onClick = onClick) {
            Icon(icon, contentDescription = label)
        }
    }
}


