package com.github.freshmorsikov.moviematcher

import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val os: OS = OS.iOS
}

actual fun getPlatform(): Platform = IOSPlatform()