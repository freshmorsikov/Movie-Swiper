package com.github.freshmorsikov.moviematcher.app

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.github.freshmorsikov.moviematcher.ExternalUriHandler
import com.github.freshmorsikov.moviematcher.app.navigation.BottomNavigationBar
import com.github.freshmorsikov.moviematcher.app.navigation.NavigationRoute
import com.github.freshmorsikov.moviematcher.app.presentation.AppUdf
import com.github.freshmorsikov.moviematcher.app.presentation.AppViewModel
import com.github.freshmorsikov.moviematcher.app.snackbar.MatchSnackbarVisuals
import com.github.freshmorsikov.moviematcher.app.snackbar.MovieSnackbarHost
import com.github.freshmorsikov.moviematcher.feature.details.MovieDetailsScreen
import com.github.freshmorsikov.moviematcher.feature.favorites.FavoriteScreen
import com.github.freshmorsikov.moviematcher.feature.matches.ui.MatchesScreen
import com.github.freshmorsikov.moviematcher.feature.name.NameScreen
import com.github.freshmorsikov.moviematcher.feature.no_connection.NoConnectionScreen
import com.github.freshmorsikov.moviematcher.feature.pairing.EntryScreen
import com.github.freshmorsikov.moviematcher.feature.pairing.PairingScreen
import com.github.freshmorsikov.moviematcher.feature.swipe.SwipeScreen
import com.github.freshmorsikov.moviematcher.util.Constants.LINK_BASE_PATH
import com.github.freshmorsikov.moviematcher.util.SubscribeOnEvents
import kotlinx.coroutines.launch
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.match_snackbar_message
import moviematcher.composeapp.generated.resources.match_snackbar_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val navController = rememberNavController()

    DisposableEffect(Unit) {
        ExternalUriHandler.listener = { uri ->
            navController.navigate(NavUri(uriString = uri))
        }
        onDispose {
            ExternalUriHandler.listener = null
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel: AppViewModel = koinViewModel()
    val scope = rememberCoroutineScope()

    val state by viewModel.state.collectAsStateWithLifecycle()

    val matchSnackbarTitle = stringResource(Res.string.match_snackbar_title)
    val matchSnackbarMessage = stringResource(Res.string.match_snackbar_message)
    SubscribeOnEvents(viewModel.event) { event ->
        when (event) {
            AppUdf.Event.NewMatchFound -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        visuals = MatchSnackbarVisuals(
                            title = matchSnackbarTitle,
                            message = matchSnackbarMessage,
                        )
                    )
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
        }
    ) { padding ->
        NavigationContainer(
            modifier = Modifier.padding(bottom = padding.calculateBottomPadding()),
            navController = navController,
        )
        MovieSnackbarHost(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .padding(horizontal = 16.dp),
            state = snackbarHostState,
            navController = navController,
        )
    }
}

@Composable
fun NavigationContainer(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavigationRoute.Entry(),
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
    ) {
        composable<NavigationRoute.Entry>(
            deepLinks = listOf(
                navDeepLink<NavigationRoute.Entry>(
                    basePath = LINK_BASE_PATH
                )
            )
        ) { backStackEntry ->
            val code = backStackEntry.toRoute<NavigationRoute.Entry>().code
            EntryScreen(
                navController = navController,
                code = code,
            )
        }
        composable<NavigationRoute.Name> {
            NameScreen(navController = navController)
        }
        composable<NavigationRoute.NoConnection> {
            NoConnectionScreen(navController = navController)
        }
        composable<NavigationRoute.Swipe> {
            SwipeScreen(navController = navController)
        }
        composable<NavigationRoute.Favorite> {
            FavoriteScreen(navController = navController)
        }
        composable<NavigationRoute.Matches> {
            MatchesScreen(navController = navController)
        }
        composable<NavigationRoute.Pairing> {
            PairingScreen(navController = navController)
        }
        composable<NavigationRoute.MovieDetails> { backStackEntry ->
            val route: NavigationRoute.MovieDetails = backStackEntry.toRoute()
            MovieDetailsScreen(
                movieId = route.movieId,
                navController = navController,
            )
        }
    }
}
