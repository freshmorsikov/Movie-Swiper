package com.github.freshmorsikov.moviematcher.shared.di

import com.github.freshmorsikov.moviematcher.shared.domain.GetCodeFlowCaseCase
import com.github.freshmorsikov.moviematcher.shared.domain.GetCodeUseCase
import com.github.freshmorsikov.moviematcher.shared.domain.GetUserUuidUseCase
import org.koin.dsl.module

val sharedDomainModule = module {
    factory {
        GetCodeFlowCaseCase(
            userRepository = get(),
        )
    }
    single {
        GetCodeUseCase(
            userRepository = get(),
        )
    }
    single {
        GetUserUuidUseCase(userRepository = get())
    }
}