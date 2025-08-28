package com.github.freshmorsikov.moviematcher.shared.domain

import com.github.freshmorsikov.moviematcher.shared.data.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow

class GetCodeFlowCaseCase(
    private val getUserUuidUseCase: GetUserUuidUseCase,
    private val getCodeUseCase: GetCodeUseCase,
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<String> {
        return flow {
            val code = getCodeUseCase()
            emit(code)

            val userUuid = getUserUuidUseCase()
            userRepository.getUserCodeFlow(userUuid = userUuid)
                .filterNotNull()
                .collect { code ->
                    emit(code)
                }
        }.distinctUntilChanged()
    }

}