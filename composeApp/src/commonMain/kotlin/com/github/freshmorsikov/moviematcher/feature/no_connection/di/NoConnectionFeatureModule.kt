package com.github.freshmorsikov.moviematcher.feature.no_connection.di

import com.github.freshmorsikov.moviematcher.feature.no_connection.data.ConnectivityRepository
import com.github.freshmorsikov.moviematcher.feature.no_connection.domain.CheckConnectivityUseCase
import org.koin.dsl.module

val noConnectionFeatureModule = module {

    single {
        ConnectivityRepository()
    }

    factory {
        CheckConnectivityUseCase(connectivityRepository = get())
    }
}
