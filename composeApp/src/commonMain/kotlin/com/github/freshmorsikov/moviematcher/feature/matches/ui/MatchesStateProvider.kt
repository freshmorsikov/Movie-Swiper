package com.github.freshmorsikov.moviematcher.feature.matches.ui

import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesUdf
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

class MatchesStateProvider: PreviewParameterProvider<MatchesUdf.State> {
    override val values: Sequence<MatchesUdf.State>
        get() = sequenceOf(
            MatchesUdf.State.Loading,
            MatchesUdf.State.Empty,
            MatchesUdf.State.Data(
                movies = List(3) { Movie.mock }
            ),
        )
}