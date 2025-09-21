package com.github.freshmorsikov.moviematcher.feature.matches.ui

import com.github.freshmorsikov.moviematcher.feature.matches.domain.model.PairState
import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesUdf
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

class MatchesStateProvider: PreviewParameterProvider<MatchesUdf.State> {
    override val values: Sequence<MatchesUdf.State>
        get() = sequenceOf(
            MatchesUdf.State.Loading,
            MatchesUdf.State.NotPaired,
            MatchesUdf.State.Empty(code = "XXXX"),
            MatchesUdf.State.Data(
                pairState = PairState.Paired(
                    code = "XXXX",
                    matchedMovieList = List(3) { Movie.mock }
                )
            ),
        )
}