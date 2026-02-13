package com.github.freshmorsikov.moviematcher.feature.name.domain

import com.github.freshmorsikov.moviematcher.shared.domain.CreateUserIfMissingUseCase

class SaveUserNameUseCase(
    private val createUserIfMissingUseCase: CreateUserIfMissingUseCase,
) {

    suspend operator fun invoke(name: String) {
        // TODO check
        //   if have user
        //   then just update
        createUserIfMissingUseCase(name = name)
    }
}
