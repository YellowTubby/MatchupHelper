package com.yellowtubby.victoryvault.domain.userdata.di

import com.yellowtubby.victoryvault.data.datamodels.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataUseCase {
    operator fun invoke(): Flow<UserData>
}