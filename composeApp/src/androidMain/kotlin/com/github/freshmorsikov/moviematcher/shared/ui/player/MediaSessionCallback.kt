package com.github.freshmorsikov.moviematcher.shared.ui.player

import androidx.media3.common.MediaItem
import androidx.media3.session.MediaSession
import com.google.common.util.concurrent.ListenableFuture

class MediaSessionCallback : MediaSession.Callback {

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        val updatedMediaItems = mediaItems.map { item ->
            item.buildUpon()
                .setUri(item.mediaId)
                .build()
        }.toMutableList()
        return super.onAddMediaItems(mediaSession, controller, updatedMediaItems)
    }

}