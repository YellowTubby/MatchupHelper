package com.yellowtubby.victoryvault.data.di

import androidx.room.Room
import com.yellowtubby.victoryvault.data.datasources.local.MatchupDatabase
import com.yellowtubby.victoryvault.data.repositories.ChampionInfoRepository
import com.yellowtubby.victoryvault.data.repositories.ChampionInfoRepositoryImpl
import com.yellowtubby.victoryvault.data.repositories.MatchupRepository
import com.yellowtubby.victoryvault.data.repositories.MatchupRepositoryImpl
import com.yellowtubby.victoryvault.data.repositories.UserRepository
import com.yellowtubby.victoryvault.data.repositories.UserRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<MatchupRepository> { MatchupRepositoryImpl() }
    single<ChampionInfoRepository> { ChampionInfoRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }

    single<MatchupDatabase> {
        Room.databaseBuilder(
            androidContext(),
            MatchupDatabase::class.java, "database-name"
        ).fallbackToDestructiveMigration().build()
    }
}