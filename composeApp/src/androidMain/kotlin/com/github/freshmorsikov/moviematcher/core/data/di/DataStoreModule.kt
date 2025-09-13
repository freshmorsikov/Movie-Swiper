package com.github.freshmorsikov.moviematcher.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.github.freshmorsikov.moviematcher.core.data.local.DATA_STORE_FILE_NAME
import com.github.freshmorsikov.moviematcher.core.data.local.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

object DataStoreProvider {

    private var dataStore: DataStore<Preferences>? = null

    fun newInstance(context: Context): DataStore<Preferences> {
        return dataStore ?: createDataStore {
            context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
        }.also { newDataStore ->
            dataStore = newDataStore
        }
    }

}

actual val dataStoreModule: Module = module {
    single<DataStore<Preferences>> {
        DataStoreProvider.newInstance(context = get<Context>())
    }
}