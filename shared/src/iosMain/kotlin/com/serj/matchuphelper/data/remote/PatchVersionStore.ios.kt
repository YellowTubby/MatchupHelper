package com.serj.matchuphelper.data.remote

import platform.Foundation.NSUserDefaults

actual class PatchVersionStore {
    private val defaults = NSUserDefaults.standardUserDefaults

    actual suspend fun getCachedPatchVersion(): String? {
        return defaults.stringForKey(KEY_PATCH_VERSION)
    }

    actual suspend fun savePatchVersion(version: String) {
        defaults.setObject(version, forKey = KEY_PATCH_VERSION)
    }

    companion object {
        private const val KEY_PATCH_VERSION = "cached_patch_version"
    }
}
