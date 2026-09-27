package com.github.freshmorsikov.moviematcher.feature.matches.di

import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetActiveMatchedMovieListFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetMatchedListFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetPairedUserFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesViewModel
import com.github.freshmorsikov.moviematcher.feature.name.domain.GetUserNameUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val matchesFeatureModule = module {
    viewModel {
        MatchesViewModel(
            getActiveMatchedMovieListFlowUseCase = get(),
            getUserNameUseCase = get(),
            getPairedUserFlowUseCase = get(),
            getInviteLinkUseCase = get(),
        )
    }
    factory {
        GetActiveMatchedMovieListFlowUseCase(
            getMatchedListFlowUseCase = get(),
            movieRepository = get(),
        )
    }
    factory {
        GetMatchedListFlowUseCase(
            getRoomFlowCaseCase = get(),
            matchRepository = get(),
        )
    }
    factory { GetUserNameUseCase(userRepository = get()) }
    factory { GetPairedUserFlowUseCase(userRepository = get()) }
}
