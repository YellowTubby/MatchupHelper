package com.yellowtubby.victoryvault.data.repositories

import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.data.datamodels.Role
import com.yellowtubby.victoryvault.data.datamodels.UserData
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCurrentUserData(): Flow<UserData>
    suspend fun updateCurrentMatchup(matchup: Matchup)
    suspend fun updateRole(role: Role)
    suspend fun updateSelectedChampion(champion: Champion)
}