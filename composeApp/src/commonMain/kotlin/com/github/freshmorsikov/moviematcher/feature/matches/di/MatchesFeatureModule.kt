package com.github.freshmorsikov.moviematcher.feature.matches.di

import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetPairFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val matchesFeatureModule = module {
    viewModel {
        MatchesViewModel(getPairFlowUseCase = get())
    }
    factory {
        GetPairFlowUseCase(
            getCodeFlowCaseCase = get(),
            matchRepository = get(),
        )
    }
}