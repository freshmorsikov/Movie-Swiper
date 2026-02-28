package com.github.freshmorsikov.moviematcher.app.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetMatchedListFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.name.domain.GetUserNameUseCase
import com.github.freshmorsikov.moviematcher.feature.no_connection.domain.CheckConnectivityUseCase
import kotlinx.coroutines.launch

class AppViewModel(
    private val getMatchedListFlowUseCase: GetMatchedListFlowUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val checkConnectivityUseCase: CheckConnectivityUseCase,
) : UdfViewModel<AppUdf.State, AppUdf.Action, AppUdf.Event>(
    initState = {
        AppUdf.State(
            newMatches = 0,
            isCurrentMatches = false,
            startupRoute = null,
        )
    }
) {

    init {
        checkStartupRoute()

        viewModelScope.launch {
            var currentSize: Int? = null
            getMatchedListFlowUseCase().collect { movies ->
                currentSize?.let { size ->
                    if (size < movies.size) {
                        onAction(AppUdf.Action.UpdateNewMatches)
                        sendEvent(AppUdf.Event.NewMatchFound)
                    }
                }
                currentSize = movies.size
            }
        }
    }

    fun checkStartupRoute() {
        viewModelScope.launch {
            val startupRoute = when {
                checkConnectivityUseCase().not() -> AppUdf.StartupRoute.NoConnection
                getUserNameUseCase().isNullOrBlank() -> AppUdf.StartupRoute.Name
                else -> AppUdf.StartupRoute.Swipe
            }
            onAction(AppUdf.Action.UpdateStartupRoute(route = startupRoute))
        }
    }

    override fun reduce(action: AppUdf.Action): AppUdf.State {
        return when (action) {
            AppUdf.Action.UpdateNewMatches -> {
                if (currentState.isCurrentMatches) {
                    currentState.copy(newMatches = 0)
                } else {
                    currentState.copy(newMatches = currentState.newMatches + 1)
                }
            }

            is AppUdf.Action.UpdateIsCurrentMatches -> {
                currentState.copy(
                    newMatches = if (action.isCurrentMatches) {
                        0
                    } else {
                        currentState.newMatches
                    },
                    isCurrentMatches = action.isCurrentMatches,
                )
            }

            is AppUdf.Action.UpdateStartupRoute -> {
                currentState.copy(startupRoute = action.route)
            }
        }
    }

    override suspend fun handleEffects(action: AppUdf.Action) {}

}
