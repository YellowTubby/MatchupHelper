package com.yellowtubby.victoryvault.domain.matchups.di

import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.domain.matchups.AddMatchUpUseCase
import com.yellowtubby.victoryvault.domain.matchups.AddMultiSelectedMatchupsUseCase
import com.yellowtubby.victoryvault.domain.matchups.GetFilteredMatchupsUseCase
import com.yellowtubby.victoryvault.domain.matchups.GetMultiSelectedMatchupsUseCase
import com.yellowtubby.victoryvault.domain.matchups.di.MatchupListUseCase
import com.yellowtubby.victoryvault.domain.matchups.RemoveMatchUpsUseCase
import com.yellowtubby.victoryvault.domain.matchups.RemoveMultiSelectedMatchupsUseCase
import com.yellowtubby.victoryvault.domain.matchups.UpdateMatchUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.dsl.module

val matchupDomainModule = module {
    single<AddMultiSelectedMatchupsUseCase> { AddMultiSelectedMatchupsUseCase(get()) }
    single<RemoveMultiSelectedMatchupsUseCase> { RemoveMultiSelectedMatchupsUseCase(get()) }
    single<GetMultiSelectedMatchupsUseCase> { GetMultiSelectedMatchupsUseCase(get()) }
    single<MatchupListUseCase> { GetFilteredMatchupsUseCase() }
    single<AddMatchUpUseCase> { AddMatchUpUseCase() }
    single<RemoveMatchUpsUseCase> { RemoveMatchUpsUseCase() }
    single<UpdateMatchUpUseCase> { UpdateMatchUpUseCase() }

    // Multi-Select State Flow
    single<MutableStateFlow<Pair<Boolean, List<Matchup>>>> { MutableStateFlow(Pair(false, emptyList())) }
}