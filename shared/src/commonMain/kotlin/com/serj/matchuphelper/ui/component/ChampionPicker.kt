package com.serj.matchuphelper.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.serj.matchuphelper.domain.model.Champion

@Composable
fun ChampionPicker(
    label: String,
    selectedChampion: Champion?,
    searchResults: List<Champion>,
    onSearch: (String) -> Unit,
    onSelect: (Champion) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        if (selectedChampion != null && !isSearching) {
            SelectedChampionCard(
                label = label,
                champion = selectedChampion,
                onChangeClick = { isSearching = true },
            )
        } else {
            OutlinedTextField(
                value = query,
                onValueChange = { newQuery ->
                    query = newQuery
                    if (newQuery.length >= 2) {
                        onSearch(newQuery)
                    }
                },
                label = { Text(label) },
                placeholder = { Text("Search champion...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            if (searchResults.isNotEmpty() && query.length >= 2) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(4.dp),
                    ) {
                        items(
                            items = searchResults.take(6),
                            key = { it.id },
                        ) { champion ->
                            ChampionSearchItem(
                                champion = champion,
                                onClick = {
                                    onSelect(champion)
                                    query = ""
                                    isSearching = false
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectedChampionCard(
    label: String,
    champion: Champion,
    onChangeClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onChangeClick() },
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AsyncImage(
                model = champion.imageUrl,
                contentDescription = champion.name,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = champion.name,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Text(
                text = "Change",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun ChampionSearchItem(
    champion: Champion,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AsyncImage(
            model = champion.imageUrl,
            contentDescription = champion.name,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Column {
            Text(
                text = champion.name,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = champion.title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
