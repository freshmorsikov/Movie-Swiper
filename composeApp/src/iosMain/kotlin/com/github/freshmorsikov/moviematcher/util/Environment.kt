package com.github.freshmorsikov.moviematcher.util

import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
actual val isRelease: Boolean = !Platform.isDebugBinary