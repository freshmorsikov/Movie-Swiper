package com.github.freshmorsikov.moviematcher.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.freshmorsikov.moviematcher.core.data.di.dataModule
import com.github.freshmorsikov.moviematcher.core.data.di.dataStoreModule
import com.github.freshmorsikov.moviematcher.core.data.di.sqlDriverModule
import com.github.freshmorsikov.moviematcher.feature.favorite.FavoriteScreen
import com.github.freshmorsikov.moviematcher.feature.favorite.di.favoriteFeatureModule
import com.github.freshmorsikov.moviematcher.feature.join.JoinPairScreen
import com.github.freshmorsikov.moviematcher.feature.matches.MatchesScreen
import com.github.freshmorsikov.moviematcher.feature.matches.di.matchesFeatureModule
import com.github.freshmorsikov.moviematcher.feature.pair.PairScreen
import com.github.freshmorsikov.moviematcher.feature.swipe.SwipeScreen
import com.github.freshmorsikov.moviematcher.feature.swipe.di.swipeFeatureModule
import org.koin.compose.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module

@Composable
fun App(contextModule: Module = module {}) {
    KoinApplication(
        application = {
            modules(
                contextModule,
                sqlDriverModule,
                dataStoreModule,
                dataModule,
                swipeFeatureModule,
                favoriteFeatureModule,
                matchesFeatureModule,
            )
        }
    ) {
        val navController = rememberNavController()
        Scaffold(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding(),
            bottomBar = {
                BottomNavigationBar(
                    navController = navController
                )
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = NavigationRoute.Swipe,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                composable<NavigationRoute.Swipe> {
                    SwipeScreen()
                }
                composable<NavigationRoute.Favorite> {
                    FavoriteScreen()
                }
                composable<NavigationRoute.Matches> {
                    MatchesScreen(navController = navController)
                }
                composable<NavigationRoute.Pair> {
                    PairScreen()
                }
                composable<NavigationRoute.JoinPair> {
                    JoinPairScreen()
                }
            }
        }
    }
}