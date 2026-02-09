package com.github.freshmorsikov.moviematcher.feature.swipe.analytics

import com.github.freshmorsikov.moviematcher.core.analytics.AnalyticsEvent

data object FetchMoviesFailedEvent: AnalyticsEvent {
    override val name: String = "fetch_movies_failed"
}