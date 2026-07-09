package com.serj.matchuphelper.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PoolEntry(
    val championId: String,
    val role: Role,
    val comfort: Int,
)
