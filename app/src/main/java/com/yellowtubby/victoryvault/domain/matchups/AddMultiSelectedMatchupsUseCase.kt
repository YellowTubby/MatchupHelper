package com.yellowtubby.victoryvault.domain.matchups

import com.yellowtubby.victoryvault.model.Matchup
import kotlinx.coroutines.flow.MutableStateFlow

open class AddMultiSelectedMatchupsUseCase(
    private val stateFlow: MutableStateFlow<Pair<Boolean, List<Matchup>>>
) {
    suspend operator fun invoke(matchup: Matchup) {
        stateFlow.value = stateFlow.value.copy(
            second = stateFlow.value.second.plus(matchup)
        )
    }

    suspend operator fun invoke() {
        stateFlow.value = stateFlow.value.copy(
            first = true
        )
    }

}