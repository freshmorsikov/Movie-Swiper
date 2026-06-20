package com.github.freshmorsikov.moviematcher.feature.watchlists

import com.github.freshmorsikov.moviematcher.feature.watchlists.domain.WatchlistType
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.watchlist_disliked
import moviematcher.composeapp.generated.resources.watchlist_liked
import moviematcher.composeapp.generated.resources.watchlist_matches
import moviematcher.composeapp.generated.resources.watchlist_watched
import org.jetbrains.compose.resources.StringResource

fun WatchlistType.titleResource(): StringResource {
    return when (this) {
        WatchlistType.Liked -> Res.string.watchlist_liked
        WatchlistType.Disliked -> Res.string.watchlist_disliked
        WatchlistType.Matches -> Res.string.watchlist_matches
        WatchlistType.Watched -> Res.string.watchlist_watched
    }
}
