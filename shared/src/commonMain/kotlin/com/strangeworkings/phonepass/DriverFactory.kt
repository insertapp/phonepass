package com.strangeworkings.phonepass
import app.cash.sqldelight.db.SqlDriver
import com.strangeworkings.phonepass.ProfileDB


expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): ProfileDB {
    val driver = driverFactory.createDriver()
    val database = ProfileDB(driver)
    return database

    // Do more work with the database (see below).
}
