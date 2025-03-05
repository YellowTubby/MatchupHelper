package com.yellowtubby.victoryvault.di

import com.yellowtubby.victoryvault.domain.champions.AddDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.champions.BaseDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.champions.ChampionListUseCase
import com.yellowtubby.victoryvault.domain.champions.GetAllChampionsUseCase
import com.yellowtubby.victoryvault.domain.champions.GetDefinedChampionsUseCase
import com.yellowtubby.victoryvault.data.datasources.local.MatchupDatabase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.koin.core.qualifier.named
import org.koin.dsl.module

val testModule = module {
    factory<TestCoroutineScheduler> { TestCoroutineScheduler() }
    single<MatchupCoroutineDispatcher> { TestCoroutineDispatcherImpl(get()) }
    factory<ScopeProvider> { TestScopeProviderImpl(get(),get()) }
    single<ChampionListUseCase> { TestDefinedChampionListUseCase() }
    single<MatchupDatabase> {
        mockk() {
            every { matchupsDao() } returns mockk(relaxed = true)
        }
    }

    single<BaseDefinedChampionUseCase>(named("add")) { mockk<AddDefinedChampionUseCase>(relaxed = true)}
    single<ChampionListUseCase>(named("defined")) { mockk<GetDefinedChampionsUseCase>(relaxed = true) }
    single<ChampionListUseCase>(named("all")) { mockk<GetAllChampionsUseCase>(relaxed = true) }

}