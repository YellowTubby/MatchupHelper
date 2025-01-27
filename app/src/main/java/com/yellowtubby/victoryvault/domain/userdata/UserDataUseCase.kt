package com.yellowtubby.victoryvault.domain.userdata

import com.yellowtubby.victoryvault.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataUseCase {
    operator fun invoke(): Flow<UserData>
}