package com.serj.matchuphelper.data.remote

expect class PatchVersionStore {
    suspend fun getCachedPatchVersion(): String?
    suspend fun savePatchVersion(version: String)
}
