package com.github.freshmorsikov.moviematcher.shared.di

import com.github.freshmorsikov.moviematcher.shared.domain.GetInviteLinkUseCase
import com.github.freshmorsikov.moviematcher.shared.domain.GetRoomFlowCaseCase
import com.github.freshmorsikov.moviematcher.shared.domain.UpdateMovieStatusUseCase
import org.koin.dsl.module

val sharedDomainModule = module {
    factory {
        GetRoomFlowCaseCase(
            roomRepository = get(),
            userRepository = get(),
        )
    }
    factory {
        GetInviteLinkUseCase(
            userRepository = get(),
        )
    }
    factory {
        UpdateMovieStatusUseCase(
            movieRepository = get(),
            userRepository = get(),
            reactionRepository = get(),
            matchRepository = get(),
        )
    }
}
