package com.github.freshmorsikov.moviematcher.feature.pairing

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.github.freshmorsikov.moviematcher.app.navigation.NavigationRoute
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.feature.pairing.presentation.EntryUdf
import com.github.freshmorsikov.moviematcher.feature.pairing.presentation.EntryViewModel
import com.github.freshmorsikov.moviematcher.util.SubscribeOnEvents
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EntryScreen(
    navController: NavController,
    code: String?,
    viewModel: EntryViewModel = koinViewModel(
        parameters = { parametersOf(code) }
    ),
) {
    SubscribeOnEvents(viewModel.event) { event ->
        when (event) {
            is EntryUdf.Event.NavigateToNoConnection -> {
                navController.navigate(NavigationRoute.NoConnection) {
                    popUpTo<NavigationRoute.Entry> { inclusive = true }
                }
            }

            is EntryUdf.Event.NavigateToName -> {
                navController.navigate(NavigationRoute.Name) {
                    popUpTo<NavigationRoute.Entry> { inclusive = true }
                }
            }

            is EntryUdf.Event.NavigateToSwipe -> {
                navController.navigate(NavigationRoute.Swipe) {
                    popUpTo<NavigationRoute.Entry> { inclusive = true }
                }
            }

            is EntryUdf.Event.NavigateToPairing -> {
                navController.navigate(NavigationRoute.Pairing) {
                    popUpTo<NavigationRoute.Entry> { inclusive = true }
                }
            }
        }
    }

    MovieScaffold {}
}
