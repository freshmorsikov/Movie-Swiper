package com.github.freshmorsikov.moviematcher.app

import com.github.freshmorsikov.moviematcher.feature.swipe.domain.CheckUserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class StartupManager(
    private val checkUserUseCase: CheckUserUseCase
) {

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Default
    )

    fun start() {
        scope.launch {
            checkUserUseCase()
        }
    }

}