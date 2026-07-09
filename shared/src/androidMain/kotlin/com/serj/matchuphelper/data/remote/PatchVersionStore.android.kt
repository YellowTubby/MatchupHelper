package com.serj.matchuphelper.data.remote

import android.content.Context

actual class PatchVersionStore(private val context: Context) {
    private val prefs by lazy {
        context.getSharedPreferences("matchup_helper_prefs", Context.MODE_PRIVATE)
    }

    actual suspend fun getCachedPatchVersion(): String? {
        return prefs.getString(KEY_PATCH_VERSION, null)
    }

    actual suspend fun savePatchVersion(version: String) {
        prefs.edit().putString(KEY_PATCH_VERSION, version).apply()
    }

    companion object {
        private const val KEY_PATCH_VERSION = "cached_patch_version"
    }
}
