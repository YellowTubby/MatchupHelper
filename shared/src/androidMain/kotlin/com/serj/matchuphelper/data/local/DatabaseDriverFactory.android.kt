package com.serj.matchuphelper.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.serj.matchuphelper.db.MatchupDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(MatchupDatabase.Schema, context, "matchup_helper.db")
    }
}
