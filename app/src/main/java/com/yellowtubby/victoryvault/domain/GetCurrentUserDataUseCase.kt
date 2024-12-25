package com.yellowtubby.victoryvault.domain

import com.yellowtubby.victoryvault.model.UserData
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import com.yellowtubby.victoryvault.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import org.koin.java.KoinJavaComponent.inject

class GetCurrentUserDataUseCase {
    protected val userRepository: UserRepository by inject(UserRepository::class.java)

    operator suspend fun invoke() : Flow<UserData>{
        return userRepository.getCurrentUserData()
    }
}