package com.serj.matchuphelper.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.serj.matchuphelper.domain.model.Outcome
import com.serj.matchuphelper.ui.screen.browse.BrowseScreen
import com.serj.matchuphelper.ui.screen.pool.PoolScreen
import com.serj.matchuphelper.ui.screen.review.DraftReview
import com.serj.matchuphelper.ui.screen.review.ReviewSetupScreen
import org.koin.compose.viewmodel.koinViewModel

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<HomeViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        HomeContent(
            uiState = uiState,
            onNewReview = { navigator.push(ReviewSetupScreen()) },
            onBrowse = { navigator.push(BrowseScreen()) },
            onPool = { navigator.push(PoolScreen()) },
            onResumeDraft = { draft -> navigator.push(ReviewSetupScreen(draft)) },
            onDeleteDraft = viewModel::deleteDraft,
            getChampionName = viewModel::getChampionName,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onNewReview: () -> Unit,
    onBrowse: () -> Unit,
    onPool: () -> Unit,
    onResumeDraft: (DraftReview) -> Unit,
    onDeleteDraft: (Long) -> Unit,
    getChampionName: (String) -> String,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Matchup Helper") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Text(
                    text = "Learn from every game you play",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            if (uiState.isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Syncing champion data...", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            uiState.error?.let { error ->
                item {
                    Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(onClick = onNewReview, modifier = Modifier.weight(1f)) {
                        Text("New Review")
                    }
                    OutlinedButton(onClick = onBrowse, modifier = Modifier.weight(1f)) {
                        Text("Browse")
                    }
                    OutlinedButton(onClick = onPool, modifier = Modifier.weight(1f)) {
                        Text("Pool")
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    StatCard(title = "Champions", value = "${uiState.championCount}", modifier = Modifier.weight(1f))
                    StatCard(title = "Reviews", value = "${uiState.reviewCount}", modifier = Modifier.weight(1f))
                }
            }

            if (uiState.drafts.isNotEmpty()) {
                item {
                    Text("Drafts", style = MaterialTheme.typography.titleMedium)
                }
                uiState.drafts.forEach { draft ->
                    item(key = "draft_${draft.id}") {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${getChampionName(draft.yourChampionId)} vs ${getChampionName(draft.enemyChampionId)}",
                                        style = MaterialTheme.typography.titleSmall,
                                    )
                                    Text(
                                        text = "${draft.role.name} • ${draft.outcome.name}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                OutlinedButton(onClick = { onDeleteDraft(draft.id) }) {
                                    Text("Delete")
                                }
                                Spacer(modifier = Modifier.padding(start = 8.dp))
                                Button(onClick = { onResumeDraft(draft) }) {
                                    Text("Resume")
                                }
                            }
                        }
                    }
                }
            }

            if (uiState.recentReviews.isNotEmpty()) {
                item {
                    Text("Recent Reviews", style = MaterialTheme.typography.titleMedium)
                }
                uiState.recentReviews.forEach { item ->
                    item(key = item.review.id) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                val outcomeColor = if (item.review.outcome == Outcome.WIN) {
                                    MaterialTheme.colorScheme.tertiary
                                } else {
                                    MaterialTheme.colorScheme.error
                                }
                                Text(text = item.review.outcome.name, style = MaterialTheme.typography.labelMedium, color = outcomeColor)
                                Spacer(modifier = Modifier.weight(1f))
                                Text(text = item.review.difficultyRating.name, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            } else if (!uiState.isLoading) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("No reviews yet", style = MaterialTheme.typography.titleSmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Complete your first post-game review to start building your matchup knowledge base.",
                                style = MaterialTheme.typography.bodySmall,
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
private fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.headlineMedium)
        }
    }
}
