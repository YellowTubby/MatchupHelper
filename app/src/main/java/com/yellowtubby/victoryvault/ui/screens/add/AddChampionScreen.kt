package com.yellowtubby.victoryvault.ui.screens.add

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.yellowtubby.victoryvault.R
import com.yellowtubby.victoryvault.ui.model.Champion
import com.yellowtubby.victoryvault.ui.screens.uicomponents.ChampionSelector
import com.yellowtubby.victoryvault.ui.screens.MatchupViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AddChampionScreen(
    scope: CoroutineScope,
    navController: NavController,
    mainViewModel: MatchupViewModel,
) {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 90.dp
    val uiState: AddChampionUiState by mainViewModel.uiStateAddChampionScreen.collectAsState()
    val champion = uiState.championSelected ?: Champion("Aatrox")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.height(statusBarHeight))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.BottomCenter
        ) {
            GlideImage(
                modifier = Modifier.wrapContentSize().padding(8.dp).clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Fit,
                model = champion.splashUri, contentDescription = "grid_icon_${champion.name}"
            )
        }
        ChampionSelector(
            uiState.allChampions
        ) {
            scope.launch {
                mainViewModel.intentChannel.trySend(
                    AddChampionIntent.ChampionSelected(
                        champion = it
                    )
                )
            }
        }

        Button(onClick = {
            scope.launch {
                mainViewModel.intentChannel.trySend(
                    AddChampionIntent.AddChampion(
                        champion = champion
                    )
                )
                navController.popBackStack()
            }
        }) {
            Text(stringResource(R.string.add_champion_string))
        }

    }
}