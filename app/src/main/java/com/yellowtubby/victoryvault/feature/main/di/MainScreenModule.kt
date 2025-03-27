package com.yellowtubby.victoryvault.feature.main.di

import com.yellowtubby.victoryvault.feature.main.presentation.screens.MainScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainScreenModule = module {
    viewModel { MainScreenViewModel() }
}