package com.yellowtubby.victoryvault.ui.uicomponents

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yellowtubby.victoryvault.ui.ApplicationUIState
import com.yellowtubby.victoryvault.ui.MAIN_ACTIVITY_STATE

@Preview
@Composable
fun MatchupProgressIndicator(
    uiState: ApplicationUIState = MAIN_ACTIVITY_STATE,
    content: @Composable () -> Unit = {}
) {
    if (uiState.loading) {
        Surface(shadowElevation = 9.dp) {
            CircularProgressIndicator(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {}
                    )
                    .wrapContentSize(Alignment.Center)
                    .size(80.dp)
            )
        }
    } else {
        content.invoke()
    }
}