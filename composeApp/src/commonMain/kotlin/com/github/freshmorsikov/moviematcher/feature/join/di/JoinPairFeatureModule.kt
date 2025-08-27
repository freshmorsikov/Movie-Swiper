package com.github.freshmorsikov.moviematcher.feature.join.di

import com.github.freshmorsikov.moviematcher.feature.join.presentation.JoinPairViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val joinPairFeatureModule = module {
    viewModel {
        JoinPairViewModel()
    }
}