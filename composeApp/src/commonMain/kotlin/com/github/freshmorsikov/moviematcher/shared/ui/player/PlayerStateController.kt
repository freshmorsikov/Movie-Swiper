package com.github.freshmorsikov.moviematcher.shared.ui.player

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class PlayerStateController {

    fun init(onStateChanged: (PlayerState) -> Unit)
    fun setContent(content: PlayerContent)
    fun play()
    fun pause()
    fun release()

}