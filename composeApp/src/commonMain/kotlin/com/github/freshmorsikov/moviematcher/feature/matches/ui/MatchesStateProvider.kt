package com.github.freshmorsikov.moviematcher.feature.matches.ui

import com.github.freshmorsikov.moviematcher.feature.matches.presentation.MatchesUdf
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import com.github.freshmorsikov.moviematcher.shared.ui.UserPairState
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

class MatchesStateProvider: PreviewParameterProvider<MatchesUdf.State> {
    override val values: Sequence<MatchesUdf.State>
        get() = sequenceOf(
            MatchesUdf.State.Loading,
            MatchesUdf.State.Empty(
                userPair = UserPairState.Paired(
                    userName = "Alan",
                    friendName = "Kate",
                    userEmoji = "\uD83D\uDC35",
                    friendEmoji = "\uD83D\uDC36",
                )
            ),
            MatchesUdf.State.Data(
                movies = List(3) { Movie.mock },
                userPair = UserPairState.Paired(
                    userName = "Alan",
                    friendName = "Kate",
                    userEmoji = "\uD83D\uDC35",
                    friendEmoji = "\uD83D\uDC36",
                ),
            ),
        )
}
