package com.serj.matchuphelper.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class DataDragonApi(private val httpClient: HttpClient) {

    suspend fun getLatestVersion(): String {
        val versions: List<String> = httpClient
            .get("https://ddragon.leagueoflegends.com/api/versions.json")
            .body()
        return versions.first()
    }

    suspend fun getChampions(version: String): List<DataDragonChampion> {
        val response: DataDragonResponse = httpClient
            .get("https://ddragon.leagueoflegends.com/cdn/$version/data/en_US/champion.json")
            .body()

        return response.data.values.map { champ ->
            DataDragonChampion(
                id = champ.id,
                name = champ.name,
                title = champ.title,
                tags = champ.tags,
                imageFileName = champ.image.full,
                version = version,
            )
        }
    }

    companion object {
        fun championImageUrl(version: String, imageFileName: String): String =
            "https://ddragon.leagueoflegends.com/cdn/$version/img/champion/$imageFileName"
    }
}

@Serializable
data class DataDragonResponse(
    val data: Map<String, DataDragonChampionDto>,
)

@Serializable
data class DataDragonChampionDto(
    val id: String,
    val name: String,
    val title: String,
    val tags: List<String>,
    val image: DataDragonImage,
)

@Serializable
data class DataDragonImage(
    val full: String,
)

data class DataDragonChampion(
    val id: String,
    val name: String,
    val title: String,
    val tags: List<String>,
    val imageFileName: String,
    val version: String,
)
