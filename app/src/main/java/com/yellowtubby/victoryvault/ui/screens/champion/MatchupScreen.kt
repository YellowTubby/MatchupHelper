package com.yellowtubby.victoryvault.ui.screens.champion

import android.animation.ArgbEvaluator
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.yellowtubby.victoryvault.R
import com.yellowtubby.victoryvault.ui.model.Ability
import com.yellowtubby.victoryvault.ui.model.AbilityType
import com.yellowtubby.victoryvault.ui.model.DamageModifier
import com.yellowtubby.victoryvault.ui.model.DamageType
import com.yellowtubby.victoryvault.ui.model.Matchup
import com.yellowtubby.victoryvault.ui.screens.MatchupViewModel
import com.yellowtubby.victoryvault.ui.screens.getIconPainerResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun MatchupScreen(
    mainViewModel: MatchupViewModel,
    scope: CoroutineScope
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .scrollable(
                state = scrollState,
                orientation = Orientation.Vertical
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        MatchupImage(mainViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        WinrateSection(mainViewModel,scope)
        Spacer(modifier = Modifier.height(16.dp))
        AbilitySection(mainViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        ItemBuildSection()
    }
}

@Composable
fun ItemBuildSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp))
    ) {
        Text(text = "Item Build")
    }
}

@Composable
fun AbilitySection(mainViewModel: MatchupViewModel) {
    val uiState: MatchupScreenUiState by mainViewModel.uiStateMatchupScreen.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp))
    ) {
        AbilityType.entries.forEach { abilityType ->
            AbilityWithText(
                ability = Ability(
                    abilityName = "Bandage Toss",
                    championName = uiState.matchup.enemy.name,
                    iconUri = buildIconURIFromTypeAndChampion(abilityType, uiState.matchup.enemy.name),
                    type = abilityType,
                    cooldownList = listOf("5", "7", "10", "15", "20"),
                    damageOrShieldList = listOf("50", "80", "100", "120", "150"),
                    modifier = DamageModifier(DamageType.AP, 60)
                )
            )
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.surfaceVariant)
        }
    }
}

fun buildIconURIFromTypeAndChampion(abilityType: AbilityType, name: String): String {
    return "https://ddragon.leagueoflegends.com/cdn/14.17.1/img/passive/Anivia_P.png"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WinrateSection(mainViewModel: MatchupViewModel, scope: CoroutineScope) {
    val uiState: MatchupScreenUiState by mainViewModel.uiStateMatchupScreen.collectAsState()
    val options = listOf("Win", "Loss")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(
                RoundedCornerShape(16.dp)
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    icon = {
                        Icon(
                            imageVector = if (index == 0) Icons.Rounded.Add else Icons.Rounded.Clear,
                            contentDescription = null,
                            modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                        )
                    },
                    onClick = {
                        scope.launch {
                            mainViewModel.intentChannel.trySend(
                                MatchupScreenIntent.WinLossChanged(index == 0)
                            )
                        }
                    },
                    selected = false
                ) {
                    Text(text = options[index])
                }
            }
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.total_games) + uiState.matchup.numTotal.toString(),
            textAlign = TextAlign.Center
        )
        // Canvas drawing for the progress indicator.
        val modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp))

        Box(modifier) {
            val trackColor = MaterialTheme.colorScheme.surface
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                drawLine(
                    start = Offset(x = 0.dp.toPx(), y = canvasHeight / 2),
                    end = Offset(x = canvasWidth, y = canvasHeight / 2),
                    color = trackColor,
                    strokeWidth = 48.dp.toPx() // instead of 5.dp.toPx() , you can also pass 5f
                )
                drawLine(
                    start = Offset(x = 0.dp.toPx(), y = canvasHeight / 2),
                    end = Offset(x = calculateProgress(uiState.matchup) * canvasWidth, y = canvasHeight / 2),
                    color = Color(ArgbEvaluator().evaluate(calculateProgress(uiState.matchup), Color.Red.toArgb(), Color.Green.toArgb()) as Int),
                    strokeWidth = 48.dp.toPx() // instead of 5.dp.toPx() , you can also pass 5f
                )
            }

            Text(
                text = "${calculateProgress(uiState.matchup) * 100}%",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

fun calculateProgress(matchup: Matchup): Float {
    return if (matchup.numTotal == 0) {
        0f
    } else {
        matchup.numWins.toFloat() / matchup.numTotal.toFloat()
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MatchupImage(mainViewModel: MatchupViewModel) {
    val uiState: MatchupScreenUiState by mainViewModel.uiStateMatchupScreen.collectAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val statusBarHeight =
            WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 90.dp
        Spacer(Modifier.height(statusBarHeight))
        Box(
            modifier = Modifier
                .wrapContentSize()
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.BottomCenter
        ) {
            val shape = RoundedCornerShape(8.dp)
            Row(
                modifier = Modifier
                    .align(alignment = Alignment.BottomStart)
                    .fillMaxSize()
                    .padding(6.dp)
                    .clip(
                        shape
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = shape
                    )
            ) {
                GlideImage(
                    modifier = Modifier.weight(1f),
                    contentScale = ContentScale.Fit,
                    model = uiState.matchup.orig.splashUri,
                    contentDescription = "grid_icon_${uiState.matchup.orig.name}"
                )
                GlideImage(
                    modifier = Modifier.weight(1f),
                    contentScale = ContentScale.Fit,
                    model = uiState.matchup.enemy.splashUri,
                    contentDescription = "grid_icon_${uiState.matchup.enemy.name}"
                )

            }
            Icon(
                modifier = Modifier.align(alignment = Alignment.Center),
                painter = getIconPainerResource(uiState.matchup.role),
                contentDescription = "role_icon_add"
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AbilityWithText(ability: Ability) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.padding(4.dp),
            contentAlignment = Alignment.BottomEnd,
        ) {
            GlideImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentScale = ContentScale.Fit,
                model = ability.iconUri,
                contentDescription = "ability_icon_${ability.championName}_${ability.type}"
            )
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ability.type.name,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column {
            Text(
                text = stringResource(R.string.cooldown) + buildAnnotatedString {
                    ability.cooldownList.forEach {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("$it / ")
                        }
                    }
                }.text.removeSuffix("/ ")
            )
            Row(modifier = Modifier) {
                Text(text = stringResource(R.string.damage_or_shield) + buildAnnotatedString {
                    ability.damageOrShieldList.forEach {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("$it / ")
                        }
                    }
                }.text.removeSuffix("/ "))
                Text(
                    text = "+ ${ability.modifier.percentage}% ${ability.modifier.type.name}",
                    color = getColorBasedOnDamageType(ability.modifier.type)
                )
            }
        }

    }
}

@Composable
fun getColorBasedOnDamageType(type: DamageType): Color {
    return when (type) {
        DamageType.AD -> Color(0xFFFFA500)
        DamageType.AP -> Color(0xFF7A6DFF)
        DamageType.Armor -> Color(0xFFFFFF00)
        DamageType.MagicResist -> Color(0xff00FFFF)
        DamageType.HP -> Color(0xFF198E54)
    }

}
