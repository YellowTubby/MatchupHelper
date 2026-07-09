package com.serj.matchuphelper.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Champion(
    val id: String,
    val name: String,
    val title: String,
    val roles: List<Role>,
    val imageUrl: String,
)
