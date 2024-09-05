package com.yellowtubby.matchuphelper.di

import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.yellowtubby.matchuphelper.repositories.ChampionInfoRepository
import com.yellowtubby.matchuphelper.repositories.ChampionInfoRepositoryImpl
import com.yellowtubby.matchuphelper.repositories.MatchupRepository
import com.yellowtubby.matchuphelper.repositories.MatchupRepositoryImpl
import com.yellowtubby.matchuphelper.room.MatchupDatabase
import com.yellowtubby.matchuphelper.ui.screens.MatchupViewModel
import org.koin.android.ext.koin.androidContext

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val matchUpModule = module {
    single<MatchupRepository> { MatchupRepositoryImpl() }
    single<ChampionInfoRepository> { ChampionInfoRepositoryImpl() }
    single<Gson> { GsonBuilder().setStrictness(Strictness.LENIENT).create() }
    single<MatchupDatabase> {
        Room.databaseBuilder(
            androidContext(),
            MatchupDatabase::class.java, "database-name"
        ).fallbackToDestructiveMigration().build()
    }
    viewModelOf(::MatchupViewModel)
    scope<MatchupViewModel> {
        scoped<MatchupCoroutineDispatcher> {
            MatchupCoroutineDispatcherImpl()
        }
    }
}