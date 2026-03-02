package com.github.freshmorsikov.moviematcher.feature.no_connection.domain

import com.github.freshmorsikov.moviematcher.feature.no_connection.data.ConnectivityRepository

class CheckConnectivityUseCase(
    private val connectivityRepository: ConnectivityRepository,
) {

    suspend operator fun invoke(): Boolean {
        return connectivityRepository.isConnected()
    }
}
