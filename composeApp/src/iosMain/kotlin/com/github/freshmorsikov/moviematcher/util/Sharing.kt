package com.github.freshmorsikov.moviematcher.util

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import org.koin.dsl.module
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIApplication
import platform.UIKit.UIActivityViewController
import platform.Foundation.NSString
import platform.Foundation.create
import platform.UIKit.UIScreen
import platform.UIKit.popoverPresentationController

actual val sharingModule = module {
    factory<SharingManager> {
        IosSharingManager()
    }
}

class IosSharingManager() : SharingManager {

    @OptIn(BetaInteropApi::class, ExperimentalForeignApi::class)
    override fun share(title: String, text: String) {
        try {
            val activityItems = listOf(
                NSString.create(string = text)
            )
            val activityViewController = UIActivityViewController(
                activityItems = activityItems,
                applicationActivities = null,
            )
            val screenBounds = UIScreen.mainScreen.bounds
            val screenSize = screenBounds.useContents { size }
            activityViewController.popoverPresentationController?.let { popover ->
                popover.sourceView = UIApplication.sharedApplication.keyWindow
                popover.sourceRect =
                    CGRectMake(
                        screenSize.width / 2.0,
                        screenSize.height / 2.0,
                        200.0,
                        50.0,
                    )
            }

            val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            requireNotNull(rootViewController) { "Could not find root view controller" }

            rootViewController.presentViewController(
                activityViewController,
                animated = true,
                completion = null,
            )
        } catch (exception: Exception) {
            throw RuntimeException("Failed to share: ${exception.message}", exception)
        }
    }

}