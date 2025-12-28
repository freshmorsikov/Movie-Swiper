package com.github.freshmorsikov.moviematcher.shared.ui.player

sealed interface PlayerState {

    data object Buffering : PlayerState
    data class Success(
        val isPaused: Boolean,
        val currentTime: Long,
        val durationTime: Long,
    ) : PlayerState

    data object Error : PlayerState

}