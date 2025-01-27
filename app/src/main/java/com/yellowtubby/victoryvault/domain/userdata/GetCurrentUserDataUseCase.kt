package com.yellowtubby.victoryvault.domain.userdata

import com.yellowtubby.victoryvault.model.UserData
import com.yellowtubby.victoryvault.repositories.UserRepository
import kotlinx.coroutines.flow.Flow


class GetCurrentUserDataUseCase (
    private val userRepository : UserRepository
): UserDataUseCase{
    override operator fun invoke() : Flow<UserData>{
        return userRepository.getCurrentUserData()
    }
}