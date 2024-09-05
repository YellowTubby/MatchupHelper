package com.yellowtubby.matchuphelper.ui.model

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import com.google.gson.Gson
import com.google.gson.Strictness
import com.google.gson.stream.JsonReader
import kotlinx.parcelize.Parcelize
import org.koin.java.KoinJavaComponent.inject
import java.io.StringReader


@Parcelize
data class Matchup(
    val orig : Champion,
    val enemy : Champion,
    val role : Role,
    val description: String,
    val difficulty: Int
) : Parcelable


class MatchupNavType : NavType<Matchup>(isNullableAllowed = false) {
    private val gson : Gson by inject(Gson::class.java)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun get(bundle: Bundle, key: String): Matchup? {
        return bundle.getParcelable("matchup", Matchup::class.java)
    }

    override fun parseValue(value: String): Matchup {
        val reader = JsonReader(StringReader(value))
        reader.strictness = Strictness.LENIENT
        return gson.fromJson(reader, Matchup::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: Matchup) {
        bundle.putParcelable(key, value)
    }
}
