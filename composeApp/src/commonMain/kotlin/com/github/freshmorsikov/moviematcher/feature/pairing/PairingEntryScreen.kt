package com.github.freshmorsikov.moviematcher.feature.pairing

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.github.freshmorsikov.moviematcher.app.navigation.NavigationRoute
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.feature.pairing.presentation.PairingEntryUdf
import com.github.freshmorsikov.moviematcher.feature.pairing.presentation.PairingEntryViewModel
import com.github.freshmorsikov.moviematcher.util.SubscribeOnEvents
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PairingEntryScreen(
    navController: NavController,
    code: String?,
    viewModel: PairingEntryViewModel = koinViewModel(
        parameters = { parametersOf(code) }
    ),
) {
    SubscribeOnEvents(viewModel.event) { event ->
        when (event) {
            is PairingEntryUdf.Event.NavigateToName -> {
                navController.navigate(
                    NavigationRoute.Name(pairingCode = event.code)
                ) {
                    popUpTo(NavigationRoute.PairingEntry::class) {
                        inclusive = true
                    }
                }
            }

            is PairingEntryUdf.Event.NavigateToPairing -> {
                navController.navigate(
                    NavigationRoute.Pairing(code = event.code)
                ) {
                    popUpTo(NavigationRoute.PairingEntry::class) {
                        inclusive = true
                    }
                }
            }
        }
    }

    MovieScaffold {}
}
