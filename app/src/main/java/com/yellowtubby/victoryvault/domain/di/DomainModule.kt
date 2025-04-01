package com.yellowtubby.victoryvault.domain.di

import com.yellowtubby.victoryvault.domain.champions.di.championDomainModule
import com.yellowtubby.victoryvault.domain.matchups.di.matchupDomainModule
import com.yellowtubby.victoryvault.domain.userdata.GetCurrentUserDataUseCase
import com.yellowtubby.victoryvault.domain.userdata.UpdateCurrentMatchupUseCase
import com.yellowtubby.victoryvault.domain.userdata.UpdateCurrentRoleUseCase
import com.yellowtubby.victoryvault.domain.userdata.UpdateCurrentSelectedChampionUseCase
import com.yellowtubby.victoryvault.domain.userdata.di.UserDataUseCase
import com.yellowtubby.victoryvault.domain.userdata.di.userDataDomainModule
import org.koin.dsl.module

val domainModule = module {
    includes(
        championDomainModule,
        matchupDomainModule,
        userDataDomainModule
    )
}