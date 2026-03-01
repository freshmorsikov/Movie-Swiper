package com.github.freshmorsikov.moviematcher.feature.no_connection.di

import com.github.freshmorsikov.moviematcher.feature.no_connection.data.ConnectivityRepository
import com.github.freshmorsikov.moviematcher.feature.no_connection.domain.CheckConnectivityUseCase
import com.github.freshmorsikov.moviematcher.feature.no_connection.presentation.NoConnectionViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val noConnectionFeatureModule = module {

    single {
        ConnectivityRepository()
    }

    viewModel {
        NoConnectionViewModel(checkConnectivityUseCase = get())
    }

    factory {
        CheckConnectivityUseCase(connectivityRepository = get())
    }
}
