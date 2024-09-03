package com.yellowtubby.matchuphelper.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.yellowtubby.matchuphelper.ui.MatchupViewModel
import com.yellowtubby.matchuphelper.ui.model.Champion
import com.yellowtubby.matchuphelper.ui.screens.matchup.MatchupIntent
import com.yellowtubby.matchuphelper.ui.screens.matchup.MatchupUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChampionCard(
    viewModel: MatchupViewModel,
    scope : CoroutineScope,
    champion: Champion,
    onClick: () -> Unit
) {
    val uiState : MatchupUiState by viewModel.uiStateMatchupScreen.collectAsState()
    var isSelected by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(top = 0.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if(uiState.isInMultiSelect){
                            isSelected = !isSelected
                        }
                        onClick()
                    },
                    onLongPress = {
                        isSelected = !isSelected
                        scope.launch {
                            viewModel.intentChannel
                                .trySend(
                                    MatchupIntent.StartMultiSelectChampion(true)
                                )
                                .also {
                                    viewModel.intentChannel.trySend(
                                        MatchupIntent.SelectChampion(champion)
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
                    .wrapContentHeight(),
                contentScale = ContentScale.FillBounds,
                model = champion.iconUri, contentDescription = "grid_icon_${champion.name}"
            )
            Text(
                text = champion.name
            )
        }
    }
}