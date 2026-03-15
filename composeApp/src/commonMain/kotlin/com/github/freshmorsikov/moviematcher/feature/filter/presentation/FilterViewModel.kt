package com.github.freshmorsikov.moviematcher.feature.filter.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.filter.domain.GetVisibleGenreListUseCase
import com.github.freshmorsikov.moviematcher.feature.filter.domain.SaveRoomGenreFilterUseCase
import com.github.freshmorsikov.moviematcher.feature.filter.domain.SelectableGenre
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.GetGenreListUseCase
import com.github.freshmorsikov.moviematcher.shared.domain.GetRoomFlowCaseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FilterViewModel(
    getGenreListUseCase: GetGenreListUseCase,
    getRoomFlowCaseCase: GetRoomFlowCaseCase,
    private val getVisibleGenreListUseCase: GetVisibleGenreListUseCase,
    private val saveRoomGenreFilterUseCase: SaveRoomGenreFilterUseCase,
) : UdfViewModel<FilterUdf.State, FilterUdf.Action, FilterUdf.Event>(
    initState = {
        FilterUdf.State.Loading
    }
) {

    init {
        flow {
            emit(getGenreListUseCase())
        }.combine(getRoomFlowCaseCase()) { genres, room ->
            genres.map { genre ->
                SelectableGenre(
                    genre = genre,
                    isSelected = genre.id in room.genreFilter,
                )
            }
        }.onEach { selectableGenres ->
            onAction(
                FilterUdf.Action.HandleLoadedGenres(
                    genres = selectableGenres,
                )
            )
        }.launchIn(viewModelScope)
    }

    override fun reduce(action: FilterUdf.Action): FilterUdf.State {
        return when (action) {
            is FilterUdf.Action.UpdateGenres -> {
                val currentSuccessState = currentState as? FilterUdf.State.Success
                val isSaving = currentSuccessState?.isSaving ?: false
                if (isSaving) {
                    return currentState
                }

                FilterUdf.State.Success(
                    isSaving = isSaving,
                    searchQuery = currentSuccessState?.searchQuery.orEmpty(),
                    initialGenres = action.currentGenres,
                    currentGenres = action.currentGenres,
                    visibleGenres = action.visibleGenres,
                    isApplyEnabled = true,
                )
            }

            is FilterUdf.Action.UpdateVisibleGenres -> {
                val currentSuccessState = currentState as? FilterUdf.State.Success ?: return currentState
                currentSuccessState.copy(
                    searchQuery = action.searchQuery,
                    visibleGenres = action.visibleGenres,
                )
            }

            is FilterUdf.Action.ToggleGenre -> {
                val currentSuccessState = currentState as? FilterUdf.State.Success ?: return currentState
                if (currentSuccessState.isSaving) {
                    return currentState
                }

                val updatedCurrentGenres = currentSuccessState.currentGenres.map { selectableGenre ->
                    if (selectableGenre.genre.id == action.genreId) {
                        selectableGenre.copy(isSelected = !selectableGenre.isSelected)
                    } else {
                        selectableGenre
                    }
                }
                val updatedVisibleGenres = currentSuccessState.visibleGenres.map { selectableGenre ->
                    if (selectableGenre.genre.id == action.genreId) {
                        selectableGenre.copy(isSelected = !selectableGenre.isSelected)
                    } else {
                        selectableGenre
                    }
                }

                currentSuccessState.copy(
                    currentGenres = updatedCurrentGenres,
                    visibleGenres = updatedVisibleGenres,
                    isApplyEnabled = updatedCurrentGenres != currentSuccessState.initialGenres,
                )
            }

            FilterUdf.Action.ApplyClick -> {
                val currentSuccessState = currentState as? FilterUdf.State.Success ?: return currentState
                currentSuccessState.copy(
                    isSaving = true,
                    isApplyEnabled = false,
                )
            }

            FilterUdf.Action.BackClick,
            FilterUdf.Action.CancelClick,
            is FilterUdf.Action.HandleLoadedGenres,
            is FilterUdf.Action.HandleSearchQuery,
                -> currentState
        }
    }

    override suspend fun handleEffects(action: FilterUdf.Action) {
        when (action) {
            is FilterUdf.Action.HandleLoadedGenres -> {
                val currentSuccessState = currentState as? FilterUdf.State.Success
                val searchQuery = currentSuccessState?.searchQuery.orEmpty()
                onAction(
                    FilterUdf.Action.UpdateGenres(
                        currentGenres = action.genres,
                        visibleGenres = getVisibleGenreListUseCase(
                            genres = action.genres,
                            searchQuery = searchQuery,
                        ),
                    )
                )
            }

            is FilterUdf.Action.HandleSearchQuery -> {
                val currentSuccessState = currentState as? FilterUdf.State.Success ?: return
                onAction(
                    FilterUdf.Action.UpdateVisibleGenres(
                        searchQuery = action.searchQuery,
                        visibleGenres = getVisibleGenreListUseCase(
                            genres = currentSuccessState.currentGenres,
                            searchQuery = action.searchQuery,
                        ),
                    )
                )
            }

            FilterUdf.Action.BackClick,
            FilterUdf.Action.CancelClick,
                -> {
                sendEvent(FilterUdf.Event.CloseScreen)
            }

            FilterUdf.Action.ApplyClick -> {
                val currentSuccessState = currentState as? FilterUdf.State.Success ?: return
                runCatching {
                    saveRoomGenreFilterUseCase(
                        selectedGenreIds = currentSuccessState.currentGenres
                            .filter(SelectableGenre::isSelected)
                            .map { genre ->
                                genre.genre.id
                            }
                    )
                }
                sendEvent(FilterUdf.Event.CloseScreen)
            }

            is FilterUdf.Action.UpdateGenres,
            is FilterUdf.Action.UpdateVisibleGenres,
            is FilterUdf.Action.ToggleGenre,
                -> Unit
        }
    }
}
