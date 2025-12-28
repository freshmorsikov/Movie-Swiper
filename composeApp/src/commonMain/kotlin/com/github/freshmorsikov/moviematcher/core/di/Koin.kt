package com.github.freshmorsikov.moviematcher.core.di

import com.github.freshmorsikov.moviematcher.app.di.appModule
import com.github.freshmorsikov.moviematcher.core.analytics.di.analyticsModule
import com.github.freshmorsikov.moviematcher.core.data.di.dataModule
import com.github.freshmorsikov.moviematcher.core.data.di.dataStoreModule
import com.github.freshmorsikov.moviematcher.core.data.di.sqlDriverModule
import com.github.freshmorsikov.moviematcher.feature.details.di.movieDetailsFeatureModule
import com.github.freshmorsikov.moviematcher.feature.favorites.di.favoritesFeatureModule
import com.github.freshmorsikov.moviematcher.feature.matches.di.matchesFeatureModule
import com.github.freshmorsikov.moviematcher.feature.pairing.di.pairingFeatureModule
import com.github.freshmorsikov.moviematcher.feature.swipe.di.swipeFeatureModule
import com.github.freshmorsikov.moviematcher.shared.di.sharedDataModule
import com.github.freshmorsikov.moviematcher.shared.di.sharedDomainModule
import com.github.freshmorsikov.moviematcher.util.sharingModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin(contextModule: Module = module {}) {
    startKoin {
        modules(
            contextModule,
            platformModule,
            appModule,
            sqlDriverModule,
            dataStoreModule,
            dataModule,
            analyticsModule,
            sharingModule,
            sharedDomainModule,
            sharedDataModule,
            swipeFeatureModule,
            favoritesFeatureModule,
            matchesFeatureModule,
            pairingFeatureModule,
            movieDetailsFeatureModule,
        )
    }
}