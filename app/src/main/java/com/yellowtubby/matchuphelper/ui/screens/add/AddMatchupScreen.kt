package com.yellowtubby.matchuphelper.ui.screens.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.yellowtubby.matchuphelper.R
import com.yellowtubby.matchuphelper.ui.screens.ChampionSelector
import com.yellowtubby.matchuphelper.ui.screens.MatchupViewModel
import com.yellowtubby.matchuphelper.ui.screens.getIconPainerResource
import com.yellowtubby.matchuphelper.ui.model.Champion
import com.yellowtubby.matchuphelper.ui.model.stringToRoleMap

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AddMatchupScreen(
    mainViewModel: MatchupViewModel,
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.BottomCenter
        ) {
            GlideImage(
                contentScale = ContentScale.Fit,
                model = champion.splashUri, contentDescription = "grid_icon_${champion.name}"
            )
            Icon(
                painter = getIconPainerResource(uiState.currentRole),
                contentDescription = "role_icon_add"
            )
        }
        ChampionSelector(
            uiState.allChampions
        ) {
        }

//            OutlinedTextField(
//                value = text,
//                onValueChange = { text = it },
//                label = { Text ("Label") },
//                minLines = 3,
//                maxLines = 3,
//                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp)
//            )

        Button(onClick = { }) {
            Text(stringResource(R.string.add_matchup_string))
        }

    }
}