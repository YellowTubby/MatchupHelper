package com.yellowtubby.victoryvault.repositories

import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.model.Role
import com.yellowtubby.victoryvault.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.last

class UserRepositoryImpl : UserRepository {
    private val userDataSharedFlow : MutableSharedFlow<UserData> = MutableSharedFlow(replay = 1)

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
        return userDataSharedFlow
    }

    override suspend fun updateCurrentMatchup(matchup: Matchup) {
        val replayEmit = userDataSharedFlow.replayCache.last()
        userDataSharedFlow.emit(replayEmit.copy(
            currentMatchup = matchup
        ))
    }
}