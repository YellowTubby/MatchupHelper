package com.yellowtubby.victoryvault.ui.screens.matchup

import android.animation.ArgbEvaluator
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.yellowtubby.victoryvault.R
import com.yellowtubby.victoryvault.model.Ability
import com.yellowtubby.victoryvault.model.DamageType
import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.ui.screens.getIconPainerResource
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenViewModel
import com.yellowtubby.victoryvault.ui.uicomponents.MatchupProgressIndicator
import com.yellowtubby.victoryvault.ui.uicomponents.TipItem
import com.yellowtubby.victoryvault.ui.uicomponents.TitleTextComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun MatchupScreen(
    scope: CoroutineScope
) {
    val mainMatchupViewModel = koinViewModel<MatchupViewModel>()
    val scrollStateTips = rememberScrollState()
    val scrollStateLayout = rememberScrollState()
    val uiState by mainMatchupViewModel.uiState.collectAsState()
    MatchupProgressIndicator(uiState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            MatchupImage(uiState)
            Spacer(modifier = Modifier.height(16.dp))
            WinrateSection(mainMatchupViewModel,scope)
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.height(1200.dp).verticalScroll(state = scrollStateLayout)) {
                Column (modifier = Modifier.border(width = 2.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp))) {
                    TitleTextComponent(AnnotatedString("Tips"))
                    Column(Modifier.padding(8.dp).height(300.dp).verticalScroll(state = scrollStateTips)) {
                        TipsSection(mainMatchupViewModel)
                    }
                }
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
            .height(700.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp))
    ) {
        TitleTextComponent(AnnotatedString("Item Section"))
        ItemBuildGrid()
    }
}

@Composable
fun ItemBuildGrid() {
    Text(modifier = Modifier.fillMaxSize(), text = "Item Build")
}

@Composable
fun TipsSection(mainViewModel: MatchupViewModel) {
    val uiState by mainViewModel.uiState.collectAsState()
    val tips by remember { mutableStateOf(mutableListOf(
        "Position carefully to avoid skill shots",
        "Use your ultimate at key team fight moments",
        "Watch your mana consumption"
    )) }
    for ( i in 1..100){
        tips.add("test$i")
    }
    LazyColumn(
        modifier = Modifier.height(300.dp).padding(8.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        itemsIndexed(tips) { index, tip ->
            TipItem(
                tip = tip,
                onDelete = { tips.removeAt(index) }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WinrateSection(mainScreenViewModel: MatchupViewModel, scope: CoroutineScope) {
    val uiState by mainScreenViewModel.uiState.collectAsState()
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
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size, baseShape = RoundedCornerShape(16.dp)),
                    icon = {
                        Icon(
                            imageVector = if (index == 0) Icons.Rounded.Add else Icons.Rounded.Clear,
                            contentDescription = null,
                            modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                        )
                    },
                    onClick = {
                        scope.launch {
                            mainScreenViewModel.emitIntent(
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
            text = stringResource(R.string.total_games) + uiState.matchup?.numTotal.toString(),
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
fun MatchupImage(uiState: MatchupScreenUIState) {
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
