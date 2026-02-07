package com.github.freshmorsikov.moviematcher

import com.github.freshmorsikov.moviematcher.app.StartupManager
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics
import dev.gitlive.firebase.crashlytics.crashlytics
import dev.gitlive.firebase.initialize
import org.koin.mp.KoinPlatform

object Start {

    // For iOS
    fun startApp() {
        startApp(appContext = null)
    }

    fun startApp(appContext: Any?) {
        setupFirebase(appContext = appContext)
        KoinPlatform.getKoin().get<StartupManager>().start()
    }

    private fun setupFirebase(appContext: Any? = null) {
        val isDebug = isDebug()
        println("Test: isDebug=$isDebug")
        if (!isDebug()) {
            Firebase.initialize(context = appContext)
            Firebase.crashlytics.setCrashlyticsCollectionEnabled(true)
            Firebase.analytics.setAnalyticsCollectionEnabled(true)
        }
    }

}