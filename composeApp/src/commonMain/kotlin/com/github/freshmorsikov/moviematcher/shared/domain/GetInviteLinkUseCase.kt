package com.github.freshmorsikov.moviematcher.shared.domain

import com.github.freshmorsikov.moviematcher.util.Constants.LINK_BASE_PATH

class GetInviteLinkUseCase(
    private val getCodeUseCase: GetCodeUseCase,
) {

    suspend operator fun invoke(): String {
        val code = getCodeUseCase()
        return "$LINK_BASE_PATH?code=$code"
    }

}
