package com.github.freshmorsikov.moviematcher.feature.name.di

import com.github.freshmorsikov.moviematcher.feature.name.presentation.NameViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val nameFeatureModule = module {
    viewModel {
        NameViewModel(userRepository = get())
    }
}
