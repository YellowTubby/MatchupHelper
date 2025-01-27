package com.yellowtubby.victoryvault.domain.userdata

import com.yellowtubby.victoryvault.model.Role
import com.yellowtubby.victoryvault.repositories.UserRepository
import org.koin.java.KoinJavaComponent.inject

class UpdateCurrentRoleUseCase {
    protected val userRepository: UserRepository by inject(UserRepository::class.java)

    operator suspend fun invoke(role: Role){
        userRepository.updateRole(role)
    }

}