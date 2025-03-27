package com.yellowtubby.victoryvault.feature.addmatchup.di

import com.yellowtubby.victoryvault.feature.addmatchup.presentation.AddMatchupViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val addMatchupModule = module {
    viewModel { AddMatchupViewModel() }
}