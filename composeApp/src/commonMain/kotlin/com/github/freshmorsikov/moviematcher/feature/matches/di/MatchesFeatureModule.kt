package com.github.freshmorsikov.moviematcher.feature.matches.di

import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetMatchedListFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetPairedUserFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.name.domain.GetUserNameUseCase
import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val matchesFeatureModule = module {
    viewModel {
        MatchesViewModel(
            getMatchedListFlowUseCase = get(),
            getUserNameUseCase = get(),
            getPairedUserFlowUseCase = get(),
            getInviteLinkUseCase = get(),
        )
    }
    factory {
        GetMatchedListFlowUseCase(
            getRoomFlowCaseCase = get(),
            matchRepository = get(),
            movieRepository = get(),
        )
    }
    factory { GetUserNameUseCase(userRepository = get()) }
    factory { GetPairedUserFlowUseCase(userRepository = get()) }
}
