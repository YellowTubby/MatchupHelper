package com.serj.matchuphelper.ui.screen.pool

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.serj.matchuphelper.domain.model.Champion
import com.serj.matchuphelper.domain.model.Role
import org.koin.compose.viewmodel.koinViewModel

class PoolScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<PoolViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        PoolContent(
            uiState = uiState,
            onBack = { navigator.pop() },
            onToggleAdd = viewModel::toggleAddMode,
            onSetAddRole = viewModel::setAddRole,
            onSearch = viewModel::searchChampions,
            onAddChampion = viewModel::addToPool,
            onRemoveChampion = viewModel::removeFromPool,
            onUpdateComfort = viewModel::updateComfort,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PoolContent(
    uiState: PoolUiState,
    onBack: () -> Unit,
    onToggleAdd: () -> Unit,
    onSetAddRole: (Role) -> Unit,
    onSearch: (String) -> Unit,
    onAddChampion: (Champion, Role) -> Unit,
    onRemoveChampion: (String, Role) -> Unit,
    onUpdateComfort: (String, Role, Int) -> Unit,
) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Champion Pool") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", style = MaterialTheme.typography.titleLarge)
                    }
                },
                actions = {
                    OutlinedButton(
                        onClick = onToggleAdd,
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        Text(if (uiState.isAddingChampion) "Cancel" else "+ Add")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = "${uiState.totalCount} champions tracked",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isAddingChampion) {
                AddChampionSection(
                    uiState = uiState,
                    onSetRole = onSetAddRole,
                    onSearch = onSearch,
                    onAdd = onAddChampion,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
            Role.entries.forEach { role ->
                val champions = uiState.entriesByRole[role].orEmpty()
                if (champions.isNotEmpty()) {
                    item(key = "header_$role") {
                        Text(
                            text = role.name,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                    items(
                        items = champions,
                        key = { "${it.entry.championId}_${it.entry.role}" },
                    ) { item ->
                        PoolChampionCard(
                            item = item,
                            onRemove = { onRemoveChampion(item.entry.championId, item.entry.role) },
                            onComfortChange = { comfort ->
                                onUpdateComfort(item.entry.championId, item.entry.role, comfort)
                            },
                        )
                    }
                }
            }

            if (uiState.entriesByRole.isEmpty() || uiState.entriesByRole.values.all { it.isEmpty() }) {
                item {
                    Text(
                        text = "No champions in your pool yet. Tap '+ Add' to start tracking.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 24.dp),
                    )
                }
            }
        }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddChampionSection(
    uiState: PoolUiState,
    onSetRole: (Role) -> Unit,
    onSearch: (String) -> Unit,
    onAdd: (Champion, Role) -> Unit,
) {
    var query by remember { mutableStateOf("") }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Add to Pool", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Role.entries.forEach { role ->
                    FilterChip(
                        selected = uiState.addRole == role,
                        onClick = { onSetRole(role) },
                        label = { Text(role.name) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    if (it.length >= 2) onSearch(it)
                },
                placeholder = { Text("Search champion...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            uiState.searchResults.take(5).forEach { champion ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onAdd(champion, uiState.addRole)
                            query = ""
                        }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AsyncImage(
                        model = champion.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                    Text(champion.name, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun PoolChampionCard(
    item: PoolChampionItem,
    onRemove: () -> Unit,
    onComfortChange: (Int) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = item.champion?.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.champion?.name ?: item.entry.championId,
                    style = MaterialTheme.typography.titleSmall,
                )
                // Comfort rating as tappable dots
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    (1..5).forEach { level ->
                        Text(
                            text = if (level <= item.entry.comfort) "●" else "○",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (level <= item.entry.comfort) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            modifier = Modifier.clickable { onComfortChange(level) },
                        )
                    }
                }
                // Matchup coverage
                if (item.matchupCount > 0) {
                    Text(
                        text = "${item.reviewedMatchupCount}/${item.matchupCount} matchups reviewed",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            IconButton(onClick = onRemove) {
                Text("X", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
