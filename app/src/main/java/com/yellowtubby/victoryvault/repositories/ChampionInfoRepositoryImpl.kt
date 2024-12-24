package com.yellowtubby.victoryvault.repositories

import android.util.Log
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.yellowtubby.victoryvault.MatchUpApplication
import com.yellowtubby.victoryvault.R
import com.yellowtubby.victoryvault.model.Champion


class ChampionInfoRepositoryImpl : ChampionInfoRepository {
    override suspend fun getAllChampions(): List<Champion> {
        val resources = MatchUpApplication.instance.resources
        val fields = R.raw::class.java.fields
        return fields.map {
            field ->
            val resourceId = field.getInt(null)
            val resourceName = resources.getResourceEntryName(resourceId)
            Champion(resourceName.capitalize(Locale.current))
        }.toList()
    }

}

