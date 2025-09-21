package com.github.freshmorsikov.moviematcher.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.github.freshmorsikov.moviematcher.app.di.appModule
import com.github.freshmorsikov.moviematcher.app.presentation.AppUdf
import com.github.freshmorsikov.moviematcher.app.presentation.AppViewModel
import com.github.freshmorsikov.moviematcher.core.analytics.di.analyticsModule
import com.github.freshmorsikov.moviematcher.core.data.di.dataModule
import com.github.freshmorsikov.moviematcher.core.data.di.dataStoreModule
import com.github.freshmorsikov.moviematcher.core.data.di.sqlDriverModule
import com.github.freshmorsikov.moviematcher.feature.code.CodeScreen
import com.github.freshmorsikov.moviematcher.feature.code.di.codeFeatureModule
import com.github.freshmorsikov.moviematcher.feature.details.MovieDetailsScreen
import com.github.freshmorsikov.moviematcher.feature.details.di.movieDetailsFeatureModule
import com.github.freshmorsikov.moviematcher.feature.favorites.FavoriteScreen
import com.github.freshmorsikov.moviematcher.feature.favorites.di.favoritesFeatureModule
import com.github.freshmorsikov.moviematcher.feature.join.JoinPairScreen
import com.github.freshmorsikov.moviematcher.feature.join.SuccessfulJoiningScreen
import com.github.freshmorsikov.moviematcher.feature.join.di.joinPairFeatureModule
import com.github.freshmorsikov.moviematcher.feature.matches.di.matchesFeatureModule
import com.github.freshmorsikov.moviematcher.feature.matches.ui.MatchesScreen
import com.github.freshmorsikov.moviematcher.feature.swipe.SwipeScreen
import com.github.freshmorsikov.moviematcher.feature.swipe.di.swipeFeatureModule
import com.github.freshmorsikov.moviematcher.shared.di.sharedDataModule
import com.github.freshmorsikov.moviematcher.shared.di.sharedDomainModule
import com.github.freshmorsikov.moviematcher.util.SubscribeOnEvents
import com.github.freshmorsikov.moviematcher.util.sharingModule
import kotlinx.coroutines.launch
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.app_new_match_found
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinMultiplatformApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration

@Composable
@OptIn(KoinExperimentalAPI::class)
fun App() {
    KoinMultiplatformApplication(
        config = KoinConfiguration {
            modules(
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
                codeFeatureModule,
                joinPairFeatureModule,
                movieDetailsFeatureModule,
            )
        }
    ) {
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }
        val viewModel: AppViewModel = koinViewModel()
        val scope = rememberCoroutineScope()

        val state by viewModel.state.collectAsStateWithLifecycle()

        val snackbarMessage = stringResource(Res.string.app_new_match_found)
        SubscribeOnEvents(viewModel.event) { event ->
            when (event) {
                AppUdf.Event.NewMatchFound -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(message = snackbarMessage)
                    }
                }
            }
        }

        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    newMatches = state.newMatches,
                    onChangeIsCurrentMatches = { isCurrent ->
                        viewModel.onAction(
                            AppUdf.Action.UpdateIsCurrentMatches(
                                isCurrentMatches = isCurrent
                            )
                        )
                    }
                )
            },
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = NavigationRoute.Swipe,
                modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
            ) {
                composable<NavigationRoute.Swipe> {
                    SwipeScreen(navController = navController)
                }
                composable<NavigationRoute.Favorite> {
                    FavoriteScreen()
                }
                composable<NavigationRoute.Matches> {
                    MatchesScreen(navController = navController)
                }
                composable<NavigationRoute.Code> {
                    CodeScreen(navController = navController)
                }
                composable<NavigationRoute.JoinPair> {
                    JoinPairScreen(navController = navController)
                }
                composable<NavigationRoute.SuccessfulJoining> {
                    SuccessfulJoiningScreen(navController = navController)
                }
                composable<NavigationRoute.MovieDetails> { backStackEntry ->
                    val route: NavigationRoute.MovieDetails = backStackEntry.toRoute()
                    MovieDetailsScreen(
                        movieId = route.movieId,
                        navController = navController,
                    )
                }
            }

            SnackbarHost(hostState = snackbarHostState)
        }
    }
}