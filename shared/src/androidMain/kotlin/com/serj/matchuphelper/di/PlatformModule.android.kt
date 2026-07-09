package com.serj.matchuphelper.di

import com.serj.matchuphelper.data.local.DatabaseDriverFactory
import com.serj.matchuphelper.data.remote.PatchVersionStore
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun platformModule(geminiApiKey: String) = module {
    single { DatabaseDriverFactory(get()) }
    single { PatchVersionStore(get()) }
    single(named("geminiApiKey")) { geminiApiKey }
}
