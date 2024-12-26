package com.yellowtubby.victoryvault.repositories

import android.util.Log
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.yellowtubby.victoryvault.MatchUpApplication
import com.yellowtubby.victoryvault.R
import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow


class ChampionInfoRepositoryImpl : ChampionInfoRepository {

    private val allChampionStateFlow : MutableStateFlow<List<Champion>> = MutableStateFlow(emptyList())

    init {
        val resources = MatchUpApplication.instance.resources
        val fields = R.raw::class.java.fields
        allChampionStateFlow.value = fields.map {
                field ->
            val resourceId = field.getInt(null)
            val resourceName = resources.getResourceEntryName(resourceId)
            Champion(resourceName.capitalize(Locale.current))
        }.toList()
    }

    override fun getAllChampions(): Flow<List<Champion>> {
        return allChampionStateFlow.asStateFlow()
    }

}

