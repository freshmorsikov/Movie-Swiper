package com.github.freshmorsikov.moviematcher.shared.ui.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PlayerStateController(
    private val context: Context,
    private val cachedPlaybackDataSourceFactory: CachedPlaybackDataSourceFactory,
) {

    private var _player: ExoPlayer? = null
    var mediaController: MediaController? = null
        private set
    private var mediaSession: MediaSession? = null

    actual fun init(onStateChanged: (PlayerState) -> Unit) {
        val player = ExoPlayer.Builder(context)
            .setMediaSourceFactory(
                cachedPlaybackDataSourceFactory.buildCacheDataSourceFactory()
            ).build()
        _player = player
        mediaSession =
            MediaSession.Builder(context, player)
                .setCallback(MediaSessionCallback())
                .build()
        mediaController = mediaSession?.token?.let {
            MediaController.Builder(
                context,
                it
            ).buildAsync().get()
        }
    }

    actual fun setContent(content: PlayerContent) {
        val mediaItem = MediaItem.fromUri(content.url)
        _player?.apply {
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    actual fun play() {
        mediaController?.play()
    }

    actual fun pause() {
        mediaController?.pause()
    }

    actual fun release() {
        _player?.release()
        mediaSession?.release()
        mediaSession = null
    }


}