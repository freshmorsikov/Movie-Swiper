package com.github.freshmorsikov.moviematcher.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.github.freshmorsikov.moviematcher.Database
import org.koin.dsl.module

actual val sqlDriverModule = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = Database.Schema,
            context = get(),
            name = "main.db"
        )
    }
}