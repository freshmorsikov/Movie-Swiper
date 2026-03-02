package com.github.freshmorsikov.moviematcher.shared.di

import com.github.freshmorsikov.moviematcher.shared.data.MatchRepository
import com.github.freshmorsikov.moviematcher.shared.data.ReactionRepository
import org.koin.dsl.module

val sharedDataModule = module {
    single {
        MatchRepository(supabaseApiService = get())
    }
    single {
        ReactionRepository(
            supabaseApiService = get(),
        )
    }
}