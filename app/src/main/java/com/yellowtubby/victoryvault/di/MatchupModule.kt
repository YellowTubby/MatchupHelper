package com.yellowtubby.victoryvault.di

import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.yellowtubby.victoryvault.domain.champions.AddDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.matchups.AddMatchUpUseCase
import com.yellowtubby.victoryvault.domain.champions.BaseDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.champions.ChampionListUseCase
import com.yellowtubby.victoryvault.domain.champions.GetAllChampionsUseCase
import com.yellowtubby.victoryvault.domain.userdata.GetCurrentUserDataUseCase
import com.yellowtubby.victoryvault.domain.champions.GetDefinedChampionsUseCase
import com.yellowtubby.victoryvault.domain.matchups.GetFilteredMatchupsUseCase
import com.yellowtubby.victoryvault.domain.champions.RemoveDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.matchups.MatchupListUseCase
import com.yellowtubby.victoryvault.domain.matchups.RemoveMatchUpsUseCase
import com.yellowtubby.victoryvault.domain.userdata.UpdateCurrentMatchupUseCase
import com.yellowtubby.victoryvault.domain.userdata.UpdateCurrentRoleUseCase
import com.yellowtubby.victoryvault.domain.userdata.UpdateCurrentSelectedChampionUseCase
import com.yellowtubby.victoryvault.domain.matchups.UpdateMatchUpUseCase
import com.yellowtubby.victoryvault.domain.userdata.UserDataUseCase
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
import org.koin.core.qualifier.named

import org.koin.dsl.module

val matchUpModule = module {
    // Repos
    single<MatchupRepository> { MatchupRepositoryImpl() }
    single<ChampionInfoRepository> { ChampionInfoRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }



//    Use cases
    single<ChampionListUseCase>(named("all")) { GetAllChampionsUseCase(get()) }
    single<ChampionListUseCase>(named("defined")) { GetDefinedChampionsUseCase() }


    single<MatchupListUseCase> { GetFilteredMatchupsUseCase() }
    single<BaseDefinedChampionUseCase>(named("add")) { AddDefinedChampionUseCase() }
    single<BaseDefinedChampionUseCase>(named("remove")) { RemoveDefinedChampionUseCase() }
    single<UserDataUseCase> { GetCurrentUserDataUseCase(get()) }
    single<AddMatchUpUseCase> { AddMatchUpUseCase() }
    single<RemoveMatchUpsUseCase> { RemoveMatchUpsUseCase() }
    single<UpdateMatchUpUseCase> { UpdateMatchUpUseCase() }
    single<UpdateCurrentMatchupUseCase> { UpdateCurrentMatchupUseCase() }
    single<UpdateCurrentRoleUseCase> { UpdateCurrentRoleUseCase() }
    single<UpdateCurrentSelectedChampionUseCase> { UpdateCurrentSelectedChampionUseCase() }
    single<SharedFlowProvider> { SharedFlowProviderImpl() }
    single<MatchupCoroutineDispatcher> { MatchupCoroutineDispatcherImpl() }
    single<ScopeProvider> { ScopeProviderImpl() }


    // 3rd Party
    single<MatchupDatabase> {
        Room.databaseBuilder(
            androidContext(),
            MatchupDatabase::class.java, "database-name"
        ).fallbackToDestructiveMigration().build()
    }
    single<Gson> { GsonBuilder().setStrictness(Strictness.LENIENT).create() }


//    View Models
    single { MatchupViewModel(get(),get()) }
    single { MainActivityViewModel(get(),get()) }
    single { MainScreenViewModel(get(),get()) }
    single { AddMatchupViewModel(get(),get()) }

    scope<ViewModel> {
        scoped { MatchupViewModel(get(),get()) } // Scoped to the ViewModel's lifecycle
        scoped { MainActivityViewModel(get(),get()) } // Scoped to the ViewModel's lifecycle
        scoped { MainScreenViewModel(get(),get()) } // Scoped to the ViewModel's lifecycle
        scoped { AddMatchupViewModel(get(),get()) } // Scoped to the ViewModel's lifecycle
    }


}