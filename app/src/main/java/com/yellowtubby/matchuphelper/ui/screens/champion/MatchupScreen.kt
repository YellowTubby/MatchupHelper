package com.yellowtubby.matchuphelper.ui.screens.champion

import android.util.Log
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.yellowtubby.matchuphelper.R
import com.yellowtubby.matchuphelper.ui.model.Ability
import com.yellowtubby.matchuphelper.ui.model.AbilityType
import com.yellowtubby.matchuphelper.ui.model.DamageModifier
import com.yellowtubby.matchuphelper.ui.model.DamageType
import com.yellowtubby.matchuphelper.ui.model.Matchup
import com.yellowtubby.matchuphelper.ui.screens.MatchupViewModel
import com.yellowtubby.matchuphelper.ui.screens.add.DifficultySlider
import com.yellowtubby.matchuphelper.ui.screens.getIconPainerResource
import com.yellowtubby.matchuphelper.ui.screens.matchup.MainScreenIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MatchupScreen(
    mainViewModel: MatchupViewModel,
    matchup: Matchup
) {
    val uiState: MatchupScreenUiState by mainViewModel.uiStateMatchupScreen.collectAsState()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    if (uiState.matchup != matchup) {
        LaunchedEffect(true) {
            scope.launch {
                mainViewModel.intentChannel.trySend(
                    MatchupScreenIntent.LoadMatchup(matchup)
                )
            }
        }
    }
    uiState.matchup?.let {
        if (matchup == it) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .scrollable(
                        state = scrollState,
                        orientation = Orientation.Vertical)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                MatchupImage(matchup)
                Spacer(modifier = Modifier.height(16.dp))
                WinrateSection()
                Spacer(modifier = Modifier.height(16.dp))
                AbilitySection(mainViewModel, scope, matchup)
                Spacer(modifier = Modifier.height(16.dp))
                ItemBuildSection()
            }
        }
    }
}

@Composable
fun ItemBuildSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp))
    ) {
        Text(text = "Item Build")
    }
}

@Composable
fun AbilitySection(mainViewModel: MatchupViewModel, scope: CoroutineScope, matchup: Matchup) {
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
                    championName = matchup.enemy.name,
                    iconUri = buildIconURIFromTypeAndChampion(abilityType, matchup.enemy.name),
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

@Composable
fun WinrateSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp))
    ) {
        Text(text = "WinrateSection")
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MatchupImage(matchup: Matchup) {
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
                    model = matchup.orig.splashUri,
                    contentDescription = "grid_icon_${matchup.orig.name}"
                )
                GlideImage(
                    modifier = Modifier.weight(1f),
                    contentScale = ContentScale.Fit,
                    model = matchup.enemy.splashUri,
                    contentDescription = "grid_icon_${matchup.enemy.name}"
                )

            }
            Icon(
                modifier = Modifier.align(alignment = Alignment.Center),
                painter = getIconPainerResource(matchup.role),
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
        Column(
            modifier = Modifier.padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                modifier = Modifier
                    .size(48.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentScale = ContentScale.Fit,
                model = ability.iconUri,
                contentDescription = "ability_icon_${ability.championName}_${ability.type}"
            )
            Text(
                text = ability.type.name,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
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
    }

}
