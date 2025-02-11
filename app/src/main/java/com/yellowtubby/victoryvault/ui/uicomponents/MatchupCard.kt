package com.yellowtubby.victoryvault.ui.uicomponents

import android.animation.ArgbEvaluator
import android.util.Log
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.placeholder
import com.yellowtubby.victoryvault.R
import com.yellowtubby.victoryvault.ui.screens.matchup.MatchupViewModel
import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.ui.screens.main.MAIN_SCREEN_INIT_STATE
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenIntent
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenUIState
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@Preview
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MatchupCard(
    uiState: MainScreenUIState = MAIN_SCREEN_INIT_STATE,
    matchup: Matchup = Matchup(),
    onClick: () -> Unit = {},
    onLongPress: (Offset) -> Unit = {}
) {
    val isSelected = uiState.selectedMatchups.any {
        it.orig.name == matchup.orig.name && it.enemy.name == matchup.enemy.name
    }
    Card(
        modifier = Modifier
            .padding(top = 0.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onClick()
                    },
                    onLongPress = onLongPress
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
                        color = Color(ArgbEvaluator().evaluate(matchup.difficulty.toFloat()/10, Color.Green.toArgb(), Color.Red.toArgb()) as Int),
                        shape = RoundedCornerShape(48.dp, 48.dp, 4.dp, 4.dp)
                    ),
                contentScale = ContentScale.FillBounds,
                loading = placeholder(R.drawable.logo),
                model = matchup.enemy.iconUri, contentDescription = "grid_icon_${matchup.enemy.name}",
            )
            Text(
                text = matchup.enemy.name
            )
        }
    }
}