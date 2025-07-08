package com.github.freshmorsikov.moviematcher.core.data.di

import com.github.freshmorsikov.moviematcher.Database
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.koin.dsl.module

actual val sqlDriverModule = module {
    single<SqlDriver> {
        NativeSqliteDriver(Database.Schema, "main.db")
    }
}