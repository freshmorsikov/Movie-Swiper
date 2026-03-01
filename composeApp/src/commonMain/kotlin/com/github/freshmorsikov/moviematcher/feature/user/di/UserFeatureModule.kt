package com.github.freshmorsikov.moviematcher.feature.user.di

import com.github.freshmorsikov.moviematcher.feature.user.data.UserRemoteDataSource
import com.github.freshmorsikov.moviematcher.feature.user.data.UserRepository
import com.github.freshmorsikov.moviematcher.feature.user.domain.CreateUserUseCase
import org.koin.dsl.module

val userFeatureModule = module {
    single {
        UserRemoteDataSource(
            supabaseClient = get(),
        )
    }
    single {
        UserRepository(
           userRemoteDataSource = get(),
           supabaseApiService = get(),
           keyValueStore = get(),
        )
    }
    factory {
        CreateUserUseCase(
            userRepository = get(),
        )
    }
}