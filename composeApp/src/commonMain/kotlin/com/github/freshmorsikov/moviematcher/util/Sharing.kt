package com.github.freshmorsikov.moviematcher.util

import org.koin.core.module.Module

interface SharingManager {
    fun share(title: String, text: String)
}

expect val sharingModule: Module