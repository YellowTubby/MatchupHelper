package com.yellowtubby.victoryvault.feature.matchupdetails.di

import com.yellowtubby.victoryvault.feature.matchupdetails.presentation.screen.MatchupViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val matchupDetailsModule = module {
    viewModel { MatchupViewModel() }
}
