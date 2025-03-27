package com.yellowtubby.victoryvault.domain.champions.di

import com.yellowtubby.victoryvault.core.domain.BaseDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.champions.AddDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.champions.ChampionListUseCase
import com.yellowtubby.victoryvault.domain.champions.GetAllChampionsUseCase
import com.yellowtubby.victoryvault.domain.champions.GetDefinedChampionsUseCase
import com.yellowtubby.victoryvault.domain.champions.RemoveDefinedChampionUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val championDomainModule = module {
    single<ChampionListUseCase>(named("all")) { GetAllChampionsUseCase(get()) }
    single<ChampionListUseCase>(named("defined")) { GetDefinedChampionsUseCase() }
    single<BaseDefinedChampionUseCase>(named("add")) { AddDefinedChampionUseCase() }
    single<BaseDefinedChampionUseCase>(named("remove")) { RemoveDefinedChampionUseCase() }
}