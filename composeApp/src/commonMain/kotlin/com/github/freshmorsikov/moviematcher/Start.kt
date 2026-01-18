package com.github.freshmorsikov.moviematcher

import com.github.freshmorsikov.moviematcher.app.StartupManager
import org.koin.mp.KoinPlatform

object Start {

    fun startApp() {
        KoinPlatform.getKoin().get<StartupManager>().start()
    }

}