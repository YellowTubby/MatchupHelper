package com.yellowtubby.victoryvault.domain.matchups

import com.yellowtubby.victoryvault.model.Matchup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class GetMultiSelectedMatchupsUseCase(
    private val stateFlow: MutableStateFlow<Pair<Boolean, List<Matchup>>>
) {
    suspend operator fun invoke(): Flow<Pair<Boolean,List<Matchup>>> {
        return stateFlow.asStateFlow()
    }
}