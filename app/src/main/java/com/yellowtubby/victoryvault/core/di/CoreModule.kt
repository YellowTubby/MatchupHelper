package com.yellowtubby.victoryvault.core.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import org.koin.dsl.module

val coreModule = module {
    single<SharedFlowProvider> { SharedFlowProviderImpl() }
    single<CoroutineDispatcherProvider> { CoroutineDispatcherProviderImpl() }
    single<ScopeProvider> { ScopeProviderImpl() }

    single<Gson> { GsonBuilder().setStrictness(Strictness.LENIENT).create() }
}