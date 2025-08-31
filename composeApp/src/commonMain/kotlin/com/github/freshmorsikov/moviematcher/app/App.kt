package com.github.freshmorsikov.moviematcher.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.github.freshmorsikov.moviematcher.app.di.appModule
import com.github.freshmorsikov.moviematcher.app.presentation.AppUdf
import com.github.freshmorsikov.moviematcher.app.presentation.AppViewModel
import com.github.freshmorsikov.moviematcher.core.data.di.dataModule
import com.github.freshmorsikov.moviematcher.core.data.di.dataStoreModule
import com.github.freshmorsikov.moviematcher.core.data.di.sqlDriverModule
import com.github.freshmorsikov.moviematcher.feature.code.CodeScreen
import com.github.freshmorsikov.moviematcher.feature.code.di.codeFeatureModule
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
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

@Composable
fun App(contextModule: Module = module {}) {
    KoinApplication(
        application = {
            modules(
                appModule,
                contextModule,
                sqlDriverModule,
                dataStoreModule,
                dataModule,
                sharingModule,
                sharedDomainModule,
                sharedDataModule,
                swipeFeatureModule,
                favoritesFeatureModule,
                matchesFeatureModule,
                codeFeatureModule,
                joinPairFeatureModule,
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
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding(),
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
                composable<NavigationRoute.Code> {
                    CodeScreen(navController = navController)
                }
                composable<NavigationRoute.JoinPair> {
                    JoinPairScreen(navController = navController)
                }
                composable<NavigationRoute.SuccessfulJoining> {
                    SuccessfulJoiningScreen(navController = navController)
                }
            }

            SnackbarHost(hostState = snackbarHostState)
        }
    }
}