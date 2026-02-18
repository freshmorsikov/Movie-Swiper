package com.github.freshmorsikov.moviematcher.shared.di

import com.github.freshmorsikov.moviematcher.shared.domain.CreateUserUseCase
import com.github.freshmorsikov.moviematcher.shared.domain.GetRoomFlowCaseCase
import com.github.freshmorsikov.moviematcher.shared.domain.GetInviteLinkUseCase
import org.koin.dsl.module

val sharedDomainModule = module {
    factory {
        GetRoomFlowCaseCase(
            userRepository = get(),
        )
    }
    factory {
        GetInviteLinkUseCase(
            userRepository = get(),
        )
    }
    factory {
        CreateUserUseCase(
            userRepository = get(),
        )
    }
}
