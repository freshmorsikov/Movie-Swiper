package com.github.freshmorsikov.moviematcher.feature.swipe.analytics

import com.github.freshmorsikov.moviematcher.core.analytics.AnalyticsEvent

data object OpenSwipeScreenEvent: AnalyticsEvent {
    override val name: String = "open_swipe_screen"
}