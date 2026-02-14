package com.github.freshmorsikov.moviematcher.feature.name.di

import com.github.freshmorsikov.moviematcher.feature.name.domain.GetUserNameUseCase
import com.github.freshmorsikov.moviematcher.feature.name.domain.SaveUserNameUseCase
import com.github.freshmorsikov.moviematcher.feature.name.presentation.NameViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val nameFeatureModule = module {
    viewModel {
        NameViewModel(
            getUserNameUseCase = get(),
            saveUserNameUseCase = get(),
        )
    }
    factory {
        GetUserNameUseCase(userRepository = get())
    }
    factory {
        SaveUserNameUseCase(
            createUserUseCase = get(),
            userRepository = get(),
        )
    }
}
