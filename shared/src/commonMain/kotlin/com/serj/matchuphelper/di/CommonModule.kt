package com.serj.matchuphelper.di

import com.serj.matchuphelper.data.ai.GeminiMatchupAiService
import com.serj.matchuphelper.data.ai.MatchupAiService
import com.serj.matchuphelper.data.local.DatabaseDriverFactory
import com.serj.matchuphelper.data.local.MatchupRepositoryImpl
import com.serj.matchuphelper.data.local.PoolRepositoryImpl
import com.serj.matchuphelper.data.remote.ChampionRepositoryImpl
import com.serj.matchuphelper.data.remote.DataDragonApi
import com.serj.matchuphelper.db.MatchupDatabase
import com.serj.matchuphelper.domain.repository.ChampionRepository
import com.serj.matchuphelper.domain.repository.MatchupRepository
import com.serj.matchuphelper.domain.repository.PoolRepository
import com.serj.matchuphelper.ui.screen.browse.BrowseViewModel
import com.serj.matchuphelper.ui.screen.detail.MatchupDetailViewModel
import com.serj.matchuphelper.ui.screen.home.HomeViewModel
import com.serj.matchuphelper.ui.screen.pool.PoolViewModel
import com.serj.matchuphelper.ui.screen.review.ReviewViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val commonModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(get())
            }
        }
    }

    single { DataDragonApi(get()) }

    single {
        val driverFactory: DatabaseDriverFactory = get()
        MatchupDatabase(driverFactory.createDriver())
    }

    single<ChampionRepository> {
        ChampionRepositoryImpl(
            database = get(),
            dataDragonApi = get(),
            patchStore = get(),
        )
    }

    single<MatchupRepository> {
        MatchupRepositoryImpl(
            database = get(),
            json = get(),
        )
    }

    single<PoolRepository> {
        PoolRepositoryImpl(database = get())
    }

    single {
        GeminiMatchupAiService(
            httpClient = get(),
            apiKey = get(named("geminiApiKey")),
            json = get(),
        )
    }

    single<MatchupAiService> { get<GeminiMatchupAiService>() }

    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { ReviewViewModel(get(), get(), get(), get(), get()) }
    viewModel { BrowseViewModel(get(), get()) }
    viewModel { params -> MatchupDetailViewModel(params.get(), get(), get()) }
    viewModel { PoolViewModel(get(), get(), get()) }
}
