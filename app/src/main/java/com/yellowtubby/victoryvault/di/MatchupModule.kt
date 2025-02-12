package com.yellowtubby.victoryvault.di

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
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
import com.yellowtubby.victoryvault.domain.matchups.AddMultiSelectedMatchupsUseCase
import com.yellowtubby.victoryvault.domain.matchups.GetMultiSelectedMatchupsUseCase
import com.yellowtubby.victoryvault.domain.matchups.MatchupListUseCase
import com.yellowtubby.victoryvault.domain.matchups.RemoveMatchUpsUseCase
import com.yellowtubby.victoryvault.domain.matchups.RemoveMultiSelectedMatchupsUseCase
import com.yellowtubby.victoryvault.domain.userdata.UpdateCurrentMatchupUseCase
import com.yellowtubby.victoryvault.domain.userdata.UpdateCurrentRoleUseCase
import com.yellowtubby.victoryvault.domain.userdata.UpdateCurrentSelectedChampionUseCase
import com.yellowtubby.victoryvault.domain.matchups.UpdateMatchUpUseCase
import com.yellowtubby.victoryvault.domain.userdata.UserDataUseCase
import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.model.Role
import com.yellowtubby.victoryvault.model.UserData
import com.yellowtubby.victoryvault.repositories.ChampionInfoRepository
import com.yellowtubby.victoryvault.repositories.ChampionInfoRepositoryImpl
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import com.yellowtubby.victoryvault.repositories.MatchupRepositoryImpl
import com.yellowtubby.victoryvault.repositories.UserRepository
import com.yellowtubby.victoryvault.repositories.UserRepositoryImpl
import com.yellowtubby.victoryvault.repositories.room.MatchupDatabase
import com.yellowtubby.victoryvault.ui.MainActivityViewModel
import com.yellowtubby.victoryvault.ui.screens.addmatchup.AddMatchupViewModel
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenIntent
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenViewModel
import com.yellowtubby.victoryvault.ui.screens.matchup.MatchupViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.core.module.Module

val matchUpModule = module {
    // Repos
    single<MatchupRepository> { MatchupRepositoryImpl() }
    single<ChampionInfoRepository> { ChampionInfoRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }



//    Use cases
    // Champion
    single<ChampionListUseCase>(named("all")) { GetAllChampionsUseCase(get()) }
    single<ChampionListUseCase>(named("defined")) { GetDefinedChampionsUseCase() }
    single<BaseDefinedChampionUseCase>(named("add")) { AddDefinedChampionUseCase() }
    single<BaseDefinedChampionUseCase>(named("remove")) { RemoveDefinedChampionUseCase() }


    val multiSelectStateFlow = MutableStateFlow<Pair<Boolean, List<Matchup>>>(
        Pair(false,emptyList())
    )

    single<MutableStateFlow<Pair<Boolean, List<Matchup>>>> { MutableStateFlow(
        Pair(false,emptyList())
    ) }
    // Matchup
    single<AddMultiSelectedMatchupsUseCase> { AddMultiSelectedMatchupsUseCase(get()) }
    single<RemoveMultiSelectedMatchupsUseCase> { RemoveMultiSelectedMatchupsUseCase(get()) }
    single<GetMultiSelectedMatchupsUseCase> { GetMultiSelectedMatchupsUseCase(get()) }
    single<MatchupListUseCase> { GetFilteredMatchupsUseCase() }
    single<AddMatchUpUseCase> { AddMatchUpUseCase() }
    single<RemoveMatchUpsUseCase> { RemoveMatchUpsUseCase() }
    single<UpdateMatchUpUseCase> { UpdateMatchUpUseCase() }

    // UserData
    single<UpdateCurrentMatchupUseCase> { UpdateCurrentMatchupUseCase() }
    single<UpdateCurrentRoleUseCase> { UpdateCurrentRoleUseCase() }
    single<UpdateCurrentSelectedChampionUseCase> { UpdateCurrentSelectedChampionUseCase() }
    single<UserDataUseCase> { GetCurrentUserDataUseCase(get()) }





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
    viewModel { MatchupViewModel() }
    viewModel { MainActivityViewModel() }
    viewModel { MainScreenViewModel() }
    viewModel { AddMatchupViewModel() }


}

// Define a Koin module for preview-specific dependencies
val previewModule: Module = module {
    // Provide mock or simplified versions of your use cases for previews
    single(named("all")) { FakeChampionListUseCase() }
    single(named("defined")) { FakeChampionListUseCase() }
    single(named("add")) { FakeBaseDefinedChampionUseCase() }
    single { FakeRemoveMatchUpsUseCase() }
    single { FakeMatchupListUseCase() }
    single { FakeUserDataUseCase() }
    single { FakeUpdateCurrentRoleUseCase() }
    single { FakeAddMultiSelectedMatchupsUseCase() }
    single { FakeRemoveMultiSelectedMatchupsUseCase() }
    single { FakeGetMultiSelectedMatchupsUseCase() }
    single { FakeUpdateCurrentMatchupUseCase() }
    single { FakeUpdateCurrentSelectedChampionUseCase() }
}

// Fake implementations for each of the use cases your ViewModel depends on
class FakeChampionListUseCase : ChampionListUseCase {
    override suspend fun invoke(): Flow<List<Champion>> {
        return flow {
            emit(emptyList())
        }
    }
}

class FakeBaseDefinedChampionUseCase : BaseDefinedChampionUseCase() {
    override suspend fun invoke(champion: Champion) {
        return
    }
}

class FakeRemoveMatchUpsUseCase : RemoveMatchUpsUseCase() {
}

class FakeMatchupListUseCase : MatchupListUseCase {
    override suspend fun invoke(): Flow<List<Matchup>> {
        return flow {
            emit(emptyList())
        }
    }
}

class FakeUserDataUseCase : UserDataUseCase {
    override fun invoke(): Flow<UserData> {
        return flow {
            UserData()
        }
    }
}

class FakeUpdateCurrentRoleUseCase : UpdateCurrentRoleUseCase()

class FakeAddMultiSelectedMatchupsUseCase : AddMultiSelectedMatchupsUseCase(stateFlow = MutableStateFlow(Pair(false, emptyList()))) {
}

class FakeRemoveMultiSelectedMatchupsUseCase : RemoveMultiSelectedMatchupsUseCase(stateFlow = MutableStateFlow(Pair(false, emptyList())))

class FakeGetMultiSelectedMatchupsUseCase : GetMultiSelectedMatchupsUseCase(stateFlow = MutableStateFlow(Pair(false, emptyList())))

class FakeUpdateCurrentMatchupUseCase : UpdateCurrentMatchupUseCase() {
}

class FakeUpdateCurrentSelectedChampionUseCase : UpdateCurrentSelectedChampionUseCase() {
}
