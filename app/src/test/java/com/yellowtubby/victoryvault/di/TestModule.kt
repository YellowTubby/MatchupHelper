package com.yellowtubby.victoryvault.di

import com.yellowtubby.victoryvault.domain.champions.AddDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.champions.BaseDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.champions.ChampionListUseCase
import com.yellowtubby.victoryvault.repositories.ChampionInfoRepository
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import com.yellowtubby.victoryvault.repositories.MatchupRepositoryImpl
import com.yellowtubby.victoryvault.repositories.UserRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.math.sin

val testModule = module {
    single<SharedFlowProvider> { SharedFlowProviderImpl() }
    single<MatchupCoroutineDispatcher> { TestCoroutineDispatcherImpl() }
    single<ScopeProvider> { TestScopeProviderImpl(get()) }
    single<ChampionListUseCase> { TestDefinedChampionListUseCase() }
    single<BaseDefinedChampionUseCase>(named("add")) { TestAddChampionUseCase() }
}