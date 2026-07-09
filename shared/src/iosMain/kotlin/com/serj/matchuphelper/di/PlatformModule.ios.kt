package com.serj.matchuphelper.di

import com.serj.matchuphelper.data.local.DatabaseDriverFactory
import com.serj.matchuphelper.data.remote.PatchVersionStore
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun platformModule(geminiApiKey: String) = module {
    single { DatabaseDriverFactory() }
    single { PatchVersionStore() }
    single(named("geminiApiKey")) { geminiApiKey }
}
