package com.github.freshmorsikov.moviematcher.core.data.di

import android.content.Context
import com.github.freshmorsikov.moviematcher.core.data.local.DATA_STORE_FILE_NAME
import com.github.freshmorsikov.moviematcher.core.data.local.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val dataStoreModule: Module = module {
    single {
        createDataStore {
            get<Context>().filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
        }
    }
}