package com.github.freshmorsikov.moviematcher.app

import org.koin.mp.KoinPlatform.getKoin

object Start {

    fun startApp() {
        getKoin().get<StartupManager>().start()
    }

}