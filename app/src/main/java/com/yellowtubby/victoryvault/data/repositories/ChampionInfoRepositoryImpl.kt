package com.yellowtubby.victoryvault.data.repositories

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.yellowtubby.victoryvault.app.presentation.MatchUpApplication
import com.yellowtubby.victoryvault.R
import com.yellowtubby.victoryvault.data.datamodels.Champion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


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

