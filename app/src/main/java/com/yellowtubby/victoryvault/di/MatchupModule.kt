package com.yellowtubby.victoryvault.di

import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.yellowtubby.victoryvault.repositories.ChampionInfoRepository
import com.yellowtubby.victoryvault.repositories.ChampionInfoRepositoryImpl
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import com.yellowtubby.victoryvault.repositories.MatchupRepositoryImpl
import com.yellowtubby.victoryvault.room.MatchupDatabase
import com.yellowtubby.victoryvault.ui.screens.MatchupViewModel
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