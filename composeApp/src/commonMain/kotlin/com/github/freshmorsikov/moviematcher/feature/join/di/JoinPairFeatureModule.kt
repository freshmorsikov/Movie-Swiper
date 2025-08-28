package com.github.freshmorsikov.moviematcher.feature.join.di

import com.github.freshmorsikov.moviematcher.feature.join.domain.SaveCodeUseCase
import com.github.freshmorsikov.moviematcher.feature.join.domain.SetPairedUseCase
import com.github.freshmorsikov.moviematcher.feature.join.presentation.JoinPairViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val joinPairFeatureModule = module {
    viewModel {
        JoinPairViewModel(
            saveCodeUseCase = get(),
            setPairedUseCase = get(),
        )
    }
    factory {
        SaveCodeUseCase(
            userRepository = get(),
            getUserUuidUseCase = get()
        )
    }
    factory {
        SetPairedUseCase(matchRepository = get())
    }
}