package com.yellowtubby.victoryvault.ui.screens.add

import android.animation.ArgbEvaluator
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.yellowtubby.victoryvault.R
import com.yellowtubby.victoryvault.ui.model.Matchup
import com.yellowtubby.victoryvault.ui.screens.uicomponents.ChampionSelector
import com.yellowtubby.victoryvault.ui.screens.MatchupViewModel
import com.yellowtubby.victoryvault.ui.screens.Route
import com.yellowtubby.victoryvault.ui.screens.getIconPainerResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AddMatchupScreen(
    mainViewModel: MatchupViewModel,
    scope: CoroutineScope,
    navController : NavController
) {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 90.dp
    val uiState: AddMatchupUiState by mainViewModel.uiStateAddMatchupScreen.collectAsState()
    val champion = uiState.currentChampion

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.height(statusBarHeight))
        ChampionSelector(
            uiState.allChampions,
            uiState.currentChampion
        ) {
            mainViewModel.intentChannel.trySend(
                AddMatchupIntent.SelectedChampion(it)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(250.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            uiState.selectedChampion?.let {
                GlideImage(
                    modifier = Modifier,
                    contentScale = ContentScale.FillBounds,
                    model = it.splashUri,
                    contentDescription = "grid_icon_${uiState.currentChampion?.name}"
                )
            }
            val shape = RoundedCornerShape(8.dp)
            Box( modifier = Modifier
                .align(alignment = Alignment.BottomStart)
                .fillMaxSize(0.4f)
                .padding(6.dp)
                .clip(
                    shape
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = shape
                )) {
                GlideImage(
                    contentScale = ContentScale.FillBounds,
                    model = champion?.splashUri, contentDescription = "grid_icon_${champion?.name}"
                )
                Icon(
                    modifier = Modifier.align(alignment = Alignment.BottomEnd),
                    painter = getIconPainerResource(uiState.currentRole),
                    contentDescription = "role_icon_add"
                )
            }
        }

        val sliderPosition = remember { mutableFloatStateOf(0f) }
        DifficultySlider(
            Modifier.padding(24.dp),
            sliderPosition.floatValue
        ) {
            sliderPosition.floatValue = it
        }

        Button(onClick = {
            scope.launch {
                mainViewModel.intentChannel.trySend(
                    AddMatchupIntent.AddMatchup(
                        Matchup(
                            orig = uiState.currentChampion!!,
                            enemy = uiState.selectedChampion!!,
                            role = uiState.currentRole!!,
                            difficulty = sliderPosition.floatValue.toInt(),
                            numWins = 0,
                            numTotal = 0,
                            description = ""
                        )
                    )
                )
                navController.navigate(route = Route.Home.route) {
                    popUpTo(Route.Home.route){
                        inclusive = true
                    }
                }
            }
        }) {
            Text(stringResource(R.string.add_matchup_string))
        }

    }
}


@Composable
fun DifficultySlider(
    modifier: Modifier,
    sliderPosition: Float,
    onValueChanged: (Float) -> Unit
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.difficulty_slider),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Slider(
            value = sliderPosition,
            onValueChange = onValueChanged,
            steps = 9,
            valueRange = 0f..10f
        )
        Text(
            text = sliderPosition.roundToInt().toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(ArgbEvaluator().evaluate(sliderPosition/10, Color.Green.toArgb(), Color.Red.toArgb()) as Int),
            style = MaterialTheme.typography.titleLarge
        )
    }
}