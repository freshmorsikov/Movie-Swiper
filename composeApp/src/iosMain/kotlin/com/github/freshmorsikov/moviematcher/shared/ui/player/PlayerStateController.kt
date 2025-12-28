package com.github.freshmorsikov.moviematcher.shared.ui.player

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVQueuePlayer
import platform.AVFoundation.currentItem
import platform.AVFoundation.currentTime
import platform.AVFoundation.duration
import platform.AVFoundation.isPlaybackBufferEmpty
import platform.AVFoundation.isPlaybackBufferFull
import platform.AVFoundation.isPlaybackLikelyToKeepUp
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.rate
import platform.CoreMedia.CMTimeGetSeconds
import platform.Foundation.NSTimer
import platform.Foundation.NSURL

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PlayerStateController {

    val avPlayer: AVQueuePlayer = AVQueuePlayer()

    actual fun init(onStateChanged: (PlayerState) -> Unit) {
        NSTimer.scheduledTimerWithTimeInterval(1.0, true) {
            onStateChanged(getPlayerState())
        }
    }

    actual fun setContent(content: PlayerContent) {
        val nsUrl = NSURL.URLWithString(content.url)
        nsUrl?.let {
            avPlayer.insertItem(
                item = AVPlayerItem(it),
                afterItem = null
            )
        }
    }

    actual fun play() {
        avPlayer.play()
    }

    actual fun pause() {
        avPlayer.pause()
    }

    actual fun release() {
        avPlayer.removeAllItems()
    }

    private fun getPlayerState(): PlayerState {
        val item = avPlayer.currentItem
        return if (item?.isPlaybackLikelyToKeepUp() == true || item?.isPlaybackBufferFull() == true) {
            PlayerState.Success(
                isPaused = isPaused(),
                currentTime = getCurrentTime(),
                durationTime = getDurationTime(),
            )
        } else if (item?.isPlaybackBufferEmpty() == true) {
            PlayerState.Buffering
        } else {
            if (avPlayer.error != null) {
                PlayerState.Error
            } else {
                PlayerState.Buffering
            }
        }
    }

    fun isPaused(): Boolean {
        return avPlayer.rate.toLong().toInt() == 0
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun getDurationTime(): Long {
        val durationTime = avPlayer.currentItem?.let { CMTimeGetSeconds(it.duration) }
        return durationTime?.toLong() ?: 0L
    }


    @OptIn(ExperimentalForeignApi::class)
    private fun getCurrentTime(): Long {
        val currentTime = avPlayer.currentItem?.let { CMTimeGetSeconds(it.currentTime()) }
        return currentTime?.toLong() ?: 0L
    }

}