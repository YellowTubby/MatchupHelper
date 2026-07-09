package com.serj.matchuphelper.ui.screen.review

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.serj.matchuphelper.domain.model.Outcome
import com.serj.matchuphelper.domain.model.Role
import com.serj.matchuphelper.ui.component.ChampionPicker
import org.koin.compose.viewmodel.koinViewModel

data class ReviewSetupScreen(private val draft: DraftReview? = null) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<ReviewViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(draft) {
            draft?.let { viewModel.resumeDraft(it) }
        }

        when (uiState.phase) {
            ReviewPhase.SETUP -> ReviewSetupContent(
                uiState = uiState,
                onBack = { navigator.pop() },
                onSearchChampions = viewModel::searchChampions,
                onSelectYourChampion = viewModel::selectYourChampion,
                onSelectEnemyChampion = viewModel::selectEnemyChampion,
                onSelectRole = viewModel::selectRole,
                onSelectOutcome = viewModel::selectOutcome,
                onStartReview = viewModel::startReview,
                canStart = viewModel.canStartReview(),
            )
            ReviewPhase.CHATTING -> ReviewChatContent(
                uiState = uiState,
                onSendMessage = viewModel::sendMessage,
                onFinishReview = viewModel::finishReview,
            )
            ReviewPhase.EXTRACTING -> ReviewExtractingContent()
            ReviewPhase.SUMMARY -> ReviewSummaryContent(
                uiState = uiState,
                onRemoveInsight = viewModel::removeInsight,
                onUpdateNotes = viewModel::updatePersonalNotes,
                onSave = viewModel::saveReview,
            )
            ReviewPhase.SAVED -> ReviewSavedContent(onDone = { navigator.pop() })
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ReviewSetupContent(
    uiState: ReviewUiState,
    onBack: () -> Unit,
    onSearchChampions: (String) -> Unit,
    onSelectYourChampion: (com.serj.matchuphelper.domain.model.Champion) -> Unit,
    onSelectEnemyChampion: (com.serj.matchuphelper.domain.model.Champion) -> Unit,
    onSelectRole: (Role) -> Unit,
    onSelectOutcome: (Outcome) -> Unit,
    onStartReview: () -> Unit,
    canStart: Boolean,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Review") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", style = MaterialTheme.typography.titleLarge)
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text("Role", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Role.entries.forEach { role ->
                    FilterChip(
                        selected = uiState.selectedRole == role,
                        onClick = { onSelectRole(role) },
                        label = { Text(role.name) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            ChampionPicker(
                label = "Your Champion",
                selectedChampion = uiState.yourChampion,
                searchResults = uiState.searchResults,
                onSearch = onSearchChampions,
                onSelect = onSelectYourChampion,
            )

            Spacer(modifier = Modifier.height(16.dp))

            ChampionPicker(
                label = "Enemy Champion",
                selectedChampion = uiState.enemyChampion,
                searchResults = uiState.searchResults,
                onSearch = onSearchChampions,
                onSelect = onSelectEnemyChampion,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Outcome", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = uiState.outcome == Outcome.WIN,
                    onClick = { onSelectOutcome(Outcome.WIN) },
                    label = { Text("Won") },
                )
                FilterChip(
                    selected = uiState.outcome == Outcome.LOSS,
                    onClick = { onSelectOutcome(Outcome.LOSS) },
                    label = { Text("Lost") },
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onStartReview,
                enabled = canStart,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Start Review")
            }

            uiState.error?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
