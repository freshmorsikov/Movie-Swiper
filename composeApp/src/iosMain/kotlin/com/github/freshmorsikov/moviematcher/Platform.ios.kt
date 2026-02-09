package com.github.freshmorsikov.moviematcher

import platform.UIKit.UIDevice
import kotlin.experimental.ExperimentalNativeApi

class IOSPlatform : KmpPlatform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val os: OS = OS.iOS
}

actual fun getPlatform(): KmpPlatform = IOSPlatform()

@OptIn(ExperimentalNativeApi::class)
actual fun isDebug(): Boolean = Platform.isDebugBinary