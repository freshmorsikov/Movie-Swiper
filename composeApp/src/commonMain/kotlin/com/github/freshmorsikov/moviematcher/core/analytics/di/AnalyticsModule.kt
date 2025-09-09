package com.github.freshmorsikov.moviematcher.core.analytics.di

import com.github.freshmorsikov.moviematcher.core.analytics.AnalyticsManager
import org.koin.dsl.module

val analyticsModule = module {
    single {
        AnalyticsManager()
    }
}