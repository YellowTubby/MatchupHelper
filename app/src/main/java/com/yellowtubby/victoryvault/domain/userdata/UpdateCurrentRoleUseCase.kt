package com.yellowtubby.victoryvault.domain.userdata

import com.yellowtubby.victoryvault.data.datamodels.Role
import com.yellowtubby.victoryvault.data.repositories.UserRepository
import org.koin.java.KoinJavaComponent.inject

open class UpdateCurrentRoleUseCase {
    protected val userRepository: UserRepository by inject(UserRepository::class.java)

    operator suspend fun invoke(role: Role){
        userRepository.updateRole(role)
    }

}