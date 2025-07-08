package com.github.freshmorsikov.moviematcher.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MovieScaffold(
    content: @Composable BoxScope.() -> Unit
){
    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            content()
        }
    }
}