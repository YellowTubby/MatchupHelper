package com.yellowtubby.victoryvault.ui.screens.uicomponents

import android.animation.ArgbEvaluator
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.yellowtubby.victoryvault.ui.screens.matchup.MatchupViewModel
import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenIntent
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenUIState
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MatchupCard(
    viewModel: MainScreenViewModel,
    scope : CoroutineScope,
    matchup: Matchup,
    difficulty: Int,
    onClick: () -> Unit
) {
    val uiState : MainScreenUIState by viewModel.uiState.collectAsState()
    var isSelected by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(top = 0.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if(uiState.multiSelectEnabled){
                            isSelected = !isSelected
                        }
                        onClick()
                    },
                    onLongPress = {
                        isSelected = !isSelected
                        scope.launch {
                            viewModel.emitIntent(
                                    MainScreenIntent.StartMultiSelectChampion(true)
                                )
                                .also {
                                    viewModel.emitIntent(
                                        MainScreenIntent.MultiSelectMatchups(matchup)
                                    )
                                }

                        }
                    }
                )
            }
            .clip(
                shape = RoundedCornerShape(48.dp, 48.dp, 4.dp, 4.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (!isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.inversePrimary
        ),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top){
            
            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight().border(
                        width = 4.dp,
                        color = Color(ArgbEvaluator().evaluate(difficulty.toFloat()/10, Color.Green.toArgb(), Color.Red.toArgb()) as Int),
                        shape = RoundedCornerShape(48.dp, 48.dp, 4.dp, 4.dp)
                    ),
                contentScale = ContentScale.FillBounds,
                model = matchup.enemy.iconUri, contentDescription = "grid_icon_${matchup.enemy.name}"
            )
            Text(
                text = matchup.enemy.name
            )
        }
    }
}