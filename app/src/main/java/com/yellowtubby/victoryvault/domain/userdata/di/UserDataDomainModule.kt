package com.yellowtubby.victoryvault.domain.userdata.di

import com.yellowtubby.victoryvault.domain.userdata.GetCurrentUserDataUseCase
import com.yellowtubby.victoryvault.domain.userdata.UpdateCurrentMatchupUseCase
import com.yellowtubby.victoryvault.domain.userdata.UpdateCurrentRoleUseCase
import com.yellowtubby.victoryvault.domain.userdata.UpdateCurrentSelectedChampionUseCase
import com.yellowtubby.victoryvault.domain.userdata.di.UserDataUseCase
import org.koin.dsl.module

val userDataDomainModule = module {
    single<UpdateCurrentMatchupUseCase> { UpdateCurrentMatchupUseCase() }
    single<UpdateCurrentRoleUseCase> { UpdateCurrentRoleUseCase() }
    single<UpdateCurrentSelectedChampionUseCase> { UpdateCurrentSelectedChampionUseCase() }
    single<UserDataUseCase> { GetCurrentUserDataUseCase(get()) }
}