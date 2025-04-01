package com.yellowtubby.victoryvault.domain.userdata

import com.yellowtubby.victoryvault.data.datamodels.UserData
import com.yellowtubby.victoryvault.data.repositories.UserRepository
import com.yellowtubby.victoryvault.domain.userdata.di.UserDataUseCase
import kotlinx.coroutines.flow.Flow


class GetCurrentUserDataUseCase (
    private val userRepository : UserRepository
): UserDataUseCase {
    override operator fun invoke() : Flow<UserData>{
        return userRepository.getCurrentUserData()
    }
}