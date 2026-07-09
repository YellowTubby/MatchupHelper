package com.serj.matchuphelper.ui.screen.detail

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.serj.matchuphelper.domain.model.Difficulty
import com.serj.matchuphelper.domain.model.Insight
import com.serj.matchuphelper.domain.model.InsightCategory
import com.serj.matchuphelper.domain.model.MatchupReview
import com.serj.matchuphelper.domain.model.Outcome
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

data class MatchupDetailScreen(val matchupId: Long) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<MatchupDetailViewModel> { parametersOf(matchupId) }
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            MatchupDetailContent(
                uiState = uiState,
                onBack = { navigator.pop() },
            )
        }
    }
}

@Composable
private fun MatchupDetailContent(
    uiState: MatchupDetailUiState,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        TextButton(onClick = onBack) {
            Text("< Back")
        }

        // Header
        Text(
            text = "${uiState.yourChampionName} vs ${uiState.enemyChampionName}",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Summary card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Summary", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text("Record", style = MaterialTheme.typography.labelSmall)
                        Text(
                            text = "${uiState.wins}W / ${uiState.losses}L",
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Difficulty", style = MaterialTheme.typography.labelSmall)
                        DifficultyText(uiState.aggregatedDifficulty)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Reviews", style = MaterialTheme.typography.labelSmall)
                        Text(
                            text = "${uiState.reviewCount}",
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Insights by category
        if (uiState.insightsByCategory.isNotEmpty()) {
            Text("Key Insights", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            uiState.insightsByCategory.forEach { (category, insights) ->
                InsightCategorySection(category = category, insights = insights)
                Spacer(modifier = Modifier.height(12.dp))
            }
        } else {
            Text(
                text = "No insights yet. Complete more reviews to build up knowledge.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Review history
        if (uiState.reviews.isNotEmpty()) {
            Text("Review History", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            uiState.reviews.forEach { review ->
                ReviewHistoryItem(review = review)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun DifficultyText(difficulty: Difficulty?) {
    val (text, color) = when (difficulty) {
        Difficulty.EASY -> "Easy" to MaterialTheme.colorScheme.tertiary
        Difficulty.HARD -> "Hard" to MaterialTheme.colorScheme.error
        Difficulty.MEDIUM -> "Medium" to MaterialTheme.colorScheme.secondary
        null -> "—" to MaterialTheme.colorScheme.onSurfaceVariant
    }
    Text(text = text, style = MaterialTheme.typography.titleLarge, color = color)
}

@Composable
private fun InsightCategorySection(
    category: InsightCategory,
    insights: List<Insight>,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = category.name.replace("_", " "),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            insights.forEach { insight ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(
                        text = "  •  ",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Column {
                        Text(
                            text = insight.text,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        insight.gamePhase?.let { phase ->
                            Text(
                                text = phase.name,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewHistoryItem(review: MatchupReview) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            val outcomeColor = if (review.outcome == Outcome.WIN) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.error
            }
            Text(
                text = review.outcome.name,
                style = MaterialTheme.typography.labelMedium,
                color = outcomeColor,
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Difficulty: ${review.difficultyRating.name}",
                    style = MaterialTheme.typography.bodySmall,
                )
                if (review.personalNotes.isNotBlank()) {
                    Text(
                        text = review.personalNotes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                    )
                }
            }
        }
    }
}
