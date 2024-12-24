package com.yellowtubby.victoryvault.di

import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.yellowtubby.victoryvault.domain.GetAllChampionsUseCase
import com.yellowtubby.victoryvault.repositories.ChampionInfoRepository
import com.yellowtubby.victoryvault.repositories.ChampionInfoRepositoryImpl
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import com.yellowtubby.victoryvault.repositories.MatchupRepositoryImpl
import com.yellowtubby.victoryvault.repositories.room.MatchupDatabase
import com.yellowtubby.victoryvault.ui.MainActivityViewModel
import com.yellowtubby.victoryvault.ui.screens.addmatchup.AddMatchupViewModel
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenViewModel
import com.yellowtubby.victoryvault.ui.screens.matchup.MatchupViewModel
import org.koin.android.ext.koin.androidContext

import org.koin.core.module.dsl.viewModelOf
import org.koin.core.scope.get
import org.koin.dsl.module

val matchUpModule = module {
    single<MatchupRepository> { MatchupRepositoryImpl() }
    single<ChampionInfoRepository> { ChampionInfoRepositoryImpl() }
    single<Gson> { GsonBuilder().setStrictness(Strictness.LENIENT).create() }
    single<GetAllChampionsUseCase> { GetAllChampionsUseCase() }
    single<MatchupDatabase> {
        Room.databaseBuilder(
            androidContext(),
            MatchupDatabase::class.java, "database-name"
        ).fallbackToDestructiveMigration().build()
    }

    single<SharedFlowProvider> { SharedFlowProviderImpl() }
    single<MatchupCoroutineDispatcher> { MatchupCoroutineDispatcherImpl() }
    single { MatchupViewModel(get(),get()) }
    single { MainActivityViewModel(get(),get()) }
    single { MainScreenViewModel(get(),get()) }
    single { AddMatchupViewModel(get(),get()) }


}