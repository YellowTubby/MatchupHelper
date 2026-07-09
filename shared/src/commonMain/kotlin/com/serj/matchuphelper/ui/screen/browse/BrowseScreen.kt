package com.serj.matchuphelper.ui.screen.browse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.serj.matchuphelper.domain.model.Difficulty
import com.serj.matchuphelper.domain.model.Matchup
import com.serj.matchuphelper.domain.model.Role
import com.serj.matchuphelper.ui.screen.detail.MatchupDetailScreen
import org.koin.compose.viewmodel.koinViewModel

class BrowseScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<BrowseViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        BrowseContent(
            uiState = uiState,
            onBack = { navigator.pop() },
            onSearchChange = viewModel::setSearchQuery,
            onRoleFilter = viewModel::setRoleFilter,
            onSortBy = viewModel::setSortBy,
            onMatchupClick = { matchup ->
                navigator.push(MatchupDetailScreen(matchup.id))
            },
            getChampionName = viewModel::getChampionName,
            getChampionImageUrl = viewModel::getChampionImageUrl,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BrowseContent(
    uiState: BrowseUiState,
    onBack: () -> Unit,
    onSearchChange: (String) -> Unit,
    onRoleFilter: (Role?) -> Unit,
    onSortBy: (SortBy) -> Unit,
    onMatchupClick: (Matchup) -> Unit,
    getChampionName: (String) -> String,
    getChampionImageUrl: (String) -> String?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        TextButton(onClick = onBack) {
            Text("< Back")
        }

        Text(
            text = "Browse Matchups",
            style = MaterialTheme.typography.headlineLarge,
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search champion...") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Role filter chips
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = uiState.roleFilter == null,
                onClick = { onRoleFilter(null) },
                label = { Text("All") },
            )
            Role.entries.forEach { role ->
                FilterChip(
                    selected = uiState.roleFilter == role,
                    onClick = { onRoleFilter(role) },
                    label = { Text(role.name) },
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Sort chips
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SortBy.entries.forEach { sort ->
                FilterChip(
                    selected = uiState.sortBy == sort,
                    onClick = { onSortBy(sort) },
                    label = {
                        Text(
                            when (sort) {
                                SortBy.RECENT -> "Recent"
                                SortBy.DIFFICULTY -> "Difficulty"
                                SortBy.REVIEW_COUNT -> "Reviews"
                            }
                        )
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.filteredMatchups.isEmpty()) {
            Text(
                text = "No matchups found. Complete a post-game review to start building your knowledge base.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 24.dp),
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    items = uiState.filteredMatchups,
                    key = { it.id },
                ) { matchup ->
                    MatchupCard(
                        matchup = matchup,
                        onClick = { onMatchupClick(matchup) },
                        getChampionName = getChampionName,
                        getChampionImageUrl = getChampionImageUrl,
                    )
                }
            }
        }
    }
}

@Composable
private fun MatchupCard(
    matchup: Matchup,
    onClick: () -> Unit,
    getChampionName: (String) -> String,
    getChampionImageUrl: (String) -> String?,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Champion icons
            AsyncImage(
                model = getChampionImageUrl(matchup.yourChampionId),
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
            Text(
                text = " vs ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            AsyncImage(
                model = getChampionImageUrl(matchup.enemyChampionId),
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${getChampionName(matchup.yourChampionId)} vs ${getChampionName(matchup.enemyChampionId)}",
                    style = MaterialTheme.typography.titleSmall,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = matchup.role.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "${matchup.reviewCount} reviews",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            DifficultyChip(matchup.aggregatedDifficulty)
        }
    }
}

@Composable
private fun DifficultyChip(difficulty: Difficulty?) {
    val (text, color) = when (difficulty) {
        Difficulty.EASY -> "Easy" to MaterialTheme.colorScheme.tertiary
        Difficulty.HARD -> "Hard" to MaterialTheme.colorScheme.error
        Difficulty.MEDIUM -> "Medium" to MaterialTheme.colorScheme.secondary
        null -> "—" to MaterialTheme.colorScheme.onSurfaceVariant
    }
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = color,
    )
}
