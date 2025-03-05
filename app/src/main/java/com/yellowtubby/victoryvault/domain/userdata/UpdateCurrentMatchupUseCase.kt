package com.yellowtubby.victoryvault.domain.userdata

import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.data.repositories.UserRepository
import org.koin.java.KoinJavaComponent.inject

open class UpdateCurrentMatchupUseCase {
    protected val userRepository: UserRepository by inject(UserRepository::class.java)

    operator suspend fun invoke(matchup: Matchup){
        userRepository.updateCurrentMatchup(matchup)
    }

}