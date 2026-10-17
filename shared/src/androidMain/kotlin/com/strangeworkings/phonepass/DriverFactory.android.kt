package com.strangeworkings.phonepass

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.strangeworkings.phonepass.ProfileDB

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(ProfileDB.Schema, context, "test.db")
    }
}