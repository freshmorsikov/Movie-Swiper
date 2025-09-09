package com.github.freshmorsikov.moviematcher.core.analytics

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics

class AnalyticsManager {

    fun sendEvent(event: AnalyticsEvent) {
        Firebase.analytics.logEvent(name = event.name)
    }

}