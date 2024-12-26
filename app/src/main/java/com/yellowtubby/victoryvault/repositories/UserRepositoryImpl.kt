package com.yellowtubby.victoryvault.repositories

import android.util.Log
import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.model.Role
import com.yellowtubby.victoryvault.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class UserRepositoryImpl : UserRepository {
    private val userDataSharedFlow : MutableStateFlow<UserData> = MutableStateFlow(UserData())

    override suspend fun updateRole(role: Role) {
        val replayEmit = userDataSharedFlow.replayCache.last()
        userDataSharedFlow.emit(replayEmit.copy(
            currentRole = role
        ))
    }

    override suspend fun updateSelectedChampion(champion: Champion) {
        val replayEmit = userDataSharedFlow.replayCache.last()
        userDataSharedFlow.emit(replayEmit.copy(
            selectedChampion = champion
        ))
    }

    override fun getCurrentUserData(): Flow<UserData> {
        if(userDataSharedFlow.replayCache.isEmpty()){
            userDataSharedFlow.tryEmit(
                UserData()
            )
        }
        return userDataSharedFlow.asStateFlow()
    }

    override suspend fun updateCurrentMatchup(matchup: Matchup) {
        val replayEmit = userDataSharedFlow.replayCache.lastOrNull()
        replayEmit?.let {
            userDataSharedFlow.emit(it.copy(
                currentMatchup = matchup
            ))
        }
    }
}