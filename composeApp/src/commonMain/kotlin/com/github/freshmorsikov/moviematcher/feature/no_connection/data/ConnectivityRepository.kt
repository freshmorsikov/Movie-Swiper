package com.github.freshmorsikov.moviematcher.feature.no_connection.data

import dev.jordond.connectivity.Connectivity

class ConnectivityRepository() {

    private val connectivityChecker = Connectivity()

    suspend fun isConnected(): Boolean {
        return connectivityChecker.status().isConnected
    }

}
