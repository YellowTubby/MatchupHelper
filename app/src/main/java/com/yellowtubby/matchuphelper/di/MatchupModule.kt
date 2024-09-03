package com.yellowtubby.matchuphelper.di

import androidx.lifecycle.ViewModel
import com.yellowtubby.matchuphelper.repositories.ChampionInfoRepository
import com.yellowtubby.matchuphelper.repositories.ChampionInfoRepositoryImpl
import com.yellowtubby.matchuphelper.repositories.MatchupRepository
import com.yellowtubby.matchuphelper.repositories.MatchupRepositoryImpl
import com.yellowtubby.matchuphelper.ui.MatchupViewModel
import org.koin.core.module.dsl.scopedOf

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val matchUpModule = module {
    single<MatchupRepository> { MatchupRepositoryImpl() }
    single<ChampionInfoRepository> { ChampionInfoRepositoryImpl() }
    viewModelOf(::MatchupViewModel)
    scope<MatchupViewModel> {
        scoped<MatchupCoroutineDispatcher> {
            MatchupCoroutineDispatcherImpl()
        }
    }
}