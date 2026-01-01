package com.github.freshmorsikov.moviematcher.shared.di

import com.github.freshmorsikov.moviematcher.shared.data.MatchRepository
import com.github.freshmorsikov.moviematcher.shared.data.UserRepository
import org.koin.dsl.module

val sharedDataModule = module {
    single {
        MatchRepository(supabaseApiService = get())
    }
    single {
        UserRepository(
            supabaseApiService = get(),
            keyValueStore = get(),
        )
    }
}