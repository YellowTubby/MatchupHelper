package com.yellowtubby.victoryvault.app.di

import com.yellowtubby.victoryvault.app.presentation.screen.MainActivityViewModel
import com.yellowtubby.victoryvault.core.di.coreModule
import com.yellowtubby.victoryvault.data.di.dataModule
import com.yellowtubby.victoryvault.domain.di.domainModule
import com.yellowtubby.victoryvault.feature.addmatchup.di.addMatchupModule
import com.yellowtubby.victoryvault.feature.main.di.mainScreenModule
import com.yellowtubby.victoryvault.feature.matchupdetails.di.matchupDetailsModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule: Module = module {
    // Include core and shared dependencies
    includes(
        coreModule,
        domainModule,
        dataModule
    )

    // Include feature-specific modules
    includes(
        addMatchupModule,
        matchupDetailsModule,
        mainScreenModule
    )

    viewModel { MainActivityViewModel() }
}