package com.yellowtubby.victoryvault.domain

import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.model.Role
import com.yellowtubby.victoryvault.repositories.UserRepository
import org.koin.java.KoinJavaComponent.inject

class UpdateCurrentSelectedChampionUseCase {
    private val userRepository: UserRepository by inject(UserRepository::class.java)

    operator suspend fun invoke(champion: Champion){
        userRepository.updateSelectedChampion(champion)
    }
}