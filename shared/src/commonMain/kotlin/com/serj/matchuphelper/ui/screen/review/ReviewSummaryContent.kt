package com.serj.matchuphelper.ui.screen.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.serj.matchuphelper.domain.model.Difficulty
import com.serj.matchuphelper.domain.model.Insight

@Composable
fun ReviewExtractingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Extracting insights...",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
fun ReviewSummaryContent(
    uiState: ReviewUiState,
    onRemoveInsight: (Int) -> Unit,
    onUpdateNotes: (String) -> Unit,
    onSave: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Review Summary",
            style = MaterialTheme.typography.headlineLarge,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${uiState.yourChampion?.name} vs ${uiState.enemyChampion?.name}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(4.dp))

        DifficultyBadge(uiState.extractedDifficulty)

        Spacer(modifier = Modifier.height(24.dp))

        // Insights
        Text(
            text = "Key Insights",
            style = MaterialTheme.typography.titleSmall,
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.extractedInsights.isEmpty()) {
            Text(
                text = "No insights extracted. You can still add personal notes below.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            uiState.extractedInsights.forEachIndexed { index, insight ->
                InsightCard(
                    insight = insight,
                    onRemove = { onRemoveInsight(index) },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Personal notes
        Text(
            text = "Personal Notes",
            style = MaterialTheme.typography.titleSmall,
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.personalNotes,
            onValueChange = onUpdateNotes,
            placeholder = { Text("Add any additional notes...") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 6,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSaving,
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(end = 8.dp),
                    strokeWidth = 2.dp,
                )
            }
            Text("Save Review")
        }

        uiState.error?.let { error ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun ReviewSavedContent(onDone: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Review Saved",
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = "Your matchup knowledge has been updated.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(onClick = onDone) {
                Text("Done")
            }
        }
    }
}

@Composable
private fun DifficultyBadge(difficulty: Difficulty?) {
    val (text, color) = when (difficulty) {
        Difficulty.EASY -> "Easy" to MaterialTheme.colorScheme.tertiary
        Difficulty.HARD -> "Hard" to MaterialTheme.colorScheme.error
        else -> "Medium" to MaterialTheme.colorScheme.secondary
    }
    Text(
        text = "Difficulty: $text",
        style = MaterialTheme.typography.labelLarge,
        color = color,
    )
}

@Composable
private fun InsightCard(
    insight: Insight,
    onRemove: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = insight.category.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    insight.gamePhase?.let { phase ->
                        Text(
                            text = phase.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = insight.text,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            IconButton(onClick = onRemove) {
                Text("X", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
