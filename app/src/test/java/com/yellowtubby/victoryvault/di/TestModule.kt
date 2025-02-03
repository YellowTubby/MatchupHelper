package com.yellowtubby.victoryvault.di

import com.yellowtubby.victoryvault.domain.champions.BaseDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.champions.ChampionListUseCase
import com.yellowtubby.victoryvault.repositories.room.MatchupDatabase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.math.sin

val testModule = module {
    single<TestCoroutineScheduler> { TestCoroutineScheduler() }
    single<MatchupCoroutineDispatcher> { TestCoroutineDispatcherImpl(get()) }
    single<ScopeProvider> { TestScopeProviderImpl(get(),get()) }
    single<ChampionListUseCase> { TestDefinedChampionListUseCase() }
    single<BaseDefinedChampionUseCase>(named("add")) { TestAddChampionUseCase() }

    single<MatchupDatabase> {
        mockk() {
            every { matchupsDao() } returns mockk(relaxed = true)
        }
    }
}