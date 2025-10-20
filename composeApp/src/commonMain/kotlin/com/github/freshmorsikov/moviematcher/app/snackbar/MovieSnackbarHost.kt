package com.github.freshmorsikov.moviematcher.app.snackbar

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun MovieSnackbarHost(
    modifier: Modifier = Modifier,
    state: SnackbarHostState,
    navController: NavController,
) {
    SnackbarHost(
        modifier = modifier,
        hostState = state,
    ) { data ->
        when (val visuals = data.visuals) {
            is MatchSnackbarVisuals -> {
                MatchSnackbar(
                    visuals = visuals,
                    navController = navController,
                    onDismiss = {
                        data.dismiss()
                    },
                )
            }
        }
    }
}