package com.yellowtubby.victoryvault.repositories

import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.model.Role
import com.yellowtubby.victoryvault.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCurrentUserData(): Flow<UserData>
    suspend fun updateCurrentMatchup(matchup: Matchup)
    suspend fun updateRole(role: Role)
    suspend fun updateSelectedChampion(champion: Champion)
}