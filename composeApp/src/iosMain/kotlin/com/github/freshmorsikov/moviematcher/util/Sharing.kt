package com.github.freshmorsikov.moviematcher.util

import kotlinx.cinterop.BetaInteropApi
import org.koin.dsl.module
import platform.UIKit.UIApplication
import platform.UIKit.UIActivityViewController
import platform.Foundation.NSString
import platform.Foundation.create

actual val sharingModule = module {
    factory<SharingManager> {
        IosSharingManager()
    }
}

class IosSharingManager() : SharingManager {

    @OptIn(BetaInteropApi::class)
    override fun share(title: String, text: String) {
        val activityItems = listOf(
            NSString.create(string = text)
        )
        val activityViewController = UIActivityViewController(
            activityItems = activityItems,
            applicationActivities = null
        )

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(
            viewControllerToPresent = activityViewController,
            animated = true,
            completion = null
        )
    }

}