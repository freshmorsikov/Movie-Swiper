package com.github.freshmorsikov.moviematcher.shared.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVPlayerLayer
import platform.AVKit.AVPlayerViewController
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun MoviePlayerView(
    modifier: Modifier,
    controller: PlayerStateController,
) {
    val playbackLayer = remember { AVPlayerLayer() }
    val avPlayerViewController = remember { AVPlayerViewController() }
    avPlayerViewController.player = remember { controller.avPlayer }
    avPlayerViewController.showsPlaybackControls = false
    playbackLayer.player =  avPlayerViewController.player

    UIKitView(
        factory = {
            val playerContainer = UIView()
            playerContainer.addSubview(avPlayerViewController.view)
            playerContainer
        },
//        onResize = { view: UIView, rect: CValue<CGRect> ->
//            CATransaction.begin()
//            CATransaction.setValue(true, kCATransactionDisableActions)
//            view.layer.setFrame(rect)
//            playbackLayer.setFrame(rect)
//            avPlayerViewController.view.layer.frame = rect
//            CATransaction.commit()
//        },
        update = {
            //avPlayerViewController.player?.play()
        },
        onRelease = {
            avPlayerViewController.player
        },
        modifier = modifier
    )
}