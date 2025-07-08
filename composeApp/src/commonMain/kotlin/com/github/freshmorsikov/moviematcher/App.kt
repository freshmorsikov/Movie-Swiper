package com.github.freshmorsikov.moviematcher

import androidx.compose.runtime.Composable
import com.github.freshmorsikov.moviematcher.core.data.di.dataModule
import com.github.freshmorsikov.moviematcher.core.data.di.dataStoreModule
import com.github.freshmorsikov.moviematcher.core.data.di.sqlDriverModule
import com.github.freshmorsikov.moviematcher.feature.swipe.SwipeScreen
import com.github.freshmorsikov.moviematcher.feature.swipe.di.swipeFeatureModule
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module

@Composable
@Preview
fun App(contextModule: Module = module {}) {
    KoinApplication(
        application = {
            modules(
                contextModule,
                sqlDriverModule,
                dataStoreModule,
                dataModule,
                swipeFeatureModule,
            )
        }
    ) {
        SwipeScreen()
    }
}