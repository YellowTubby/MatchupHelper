package com.yellowtubby.victoryvault.di

import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.yellowtubby.victoryvault.domain.AddDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.AddMatchUpUseCase
import com.yellowtubby.victoryvault.domain.BaseDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.GetAllChampionsUseCase
import com.yellowtubby.victoryvault.domain.GetCurrentUserDataUseCase
import com.yellowtubby.victoryvault.domain.GetDefinedChampionsUseCase
import com.yellowtubby.victoryvault.domain.GetFilteredMatchupsUseCase
import com.yellowtubby.victoryvault.domain.RemoveDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.RemoveMatchUpsUseCase
import com.yellowtubby.victoryvault.domain.UpdateCurrentMatchupUseCase
import com.yellowtubby.victoryvault.domain.UpdateCurrentRoleUseCase
import com.yellowtubby.victoryvault.domain.UpdateCurrentSelectedChampionUseCase
import com.yellowtubby.victoryvault.domain.UpdateMatchUpUseCase
import com.yellowtubby.victoryvault.repositories.ChampionInfoRepository
import com.yellowtubby.victoryvault.repositories.ChampionInfoRepositoryImpl
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import com.yellowtubby.victoryvault.repositories.MatchupRepositoryImpl
import com.yellowtubby.victoryvault.repositories.UserRepository
import com.yellowtubby.victoryvault.repositories.UserRepositoryImpl
import com.yellowtubby.victoryvault.repositories.room.MatchupDatabase
import com.yellowtubby.victoryvault.ui.MainActivityViewModel
import com.yellowtubby.victoryvault.ui.screens.addmatchup.AddMatchupViewModel
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenViewModel
import com.yellowtubby.victoryvault.ui.screens.matchup.MatchupViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named

import org.koin.dsl.module

val matchUpModule = module {
    // Repos
    single<MatchupRepository> { MatchupRepositoryImpl() }
    single<ChampionInfoRepository> { ChampionInfoRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }


//    Use cases
    single<GetAllChampionsUseCase> { GetAllChampionsUseCase(get()) }
    single<GetFilteredMatchupsUseCase> { GetFilteredMatchupsUseCase() }
    single<GetDefinedChampionsUseCase> { GetDefinedChampionsUseCase() }
    single<BaseDefinedChampionUseCase>(named("add")) { AddDefinedChampionUseCase() }
    single<BaseDefinedChampionUseCase>(named("remove")) { RemoveDefinedChampionUseCase() }
    single<AddMatchUpUseCase> { AddMatchUpUseCase() }
    single<RemoveMatchUpsUseCase> { RemoveMatchUpsUseCase() }
    single<UpdateMatchUpUseCase> { UpdateMatchUpUseCase() }
    single<UpdateCurrentMatchupUseCase> { UpdateCurrentMatchupUseCase() }
    single<UpdateCurrentRoleUseCase> { UpdateCurrentRoleUseCase() }
    single<UpdateCurrentSelectedChampionUseCase> { UpdateCurrentSelectedChampionUseCase() }
    single<GetCurrentUserDataUseCase> { GetCurrentUserDataUseCase(get()) }
    single<SharedFlowProvider> { SharedFlowProviderImpl() }
    single<MatchupCoroutineDispatcher> { MatchupCoroutineDispatcherImpl() }

    // 3rd Party
    single<MatchupDatabase> {
        Room.databaseBuilder(
            androidContext(),
            MatchupDatabase::class.java, "database-name"
        ).fallbackToDestructiveMigration().build()
    }
    single<Gson> { GsonBuilder().setStrictness(Strictness.LENIENT).create() }


//    View Models
    viewModel { MatchupViewModel(get(),get()) }
    viewModel { MainActivityViewModel(get(),get()) }
    viewModel { MainScreenViewModel(get(),get(),get()) }
    viewModel { AddMatchupViewModel(get(),get()) }


}