package com.yellowtubby.victoryvault.domain.userdata

import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.repositories.UserRepository
import org.koin.java.KoinJavaComponent.inject

class UpdateCurrentMatchupUseCase {
    protected val userRepository: UserRepository by inject(UserRepository::class.java)

    operator suspend fun invoke(matchup: Matchup){
        userRepository.updateCurrentMatchup(matchup)
    }

}