package com.serj.matchuphelper.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.serj.matchuphelper.db.MatchupDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(MatchupDatabase.Schema, "matchup_helper.db")
    }
}
