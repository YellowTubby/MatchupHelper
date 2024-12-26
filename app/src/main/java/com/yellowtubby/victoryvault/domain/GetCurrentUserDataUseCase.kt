package com.yellowtubby.victoryvault.domain

import com.yellowtubby.victoryvault.MatchUpApplication
import com.yellowtubby.victoryvault.model.UserData
import com.yellowtubby.victoryvault.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class GetCurrentUserDataUseCase(
    private val userRepository : UserRepository
){
    operator fun invoke() : Flow<UserData>{
        return userRepository.getCurrentUserData()
    }
}