package com.github.freshmorsikov.moviematcher.shared.ui

import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

class UserPairStatePreviewProvider : PreviewParameterProvider<UserPairState> {
    override val values: Sequence<UserPairState>
        get() = sequenceOf(
            UserPairState.Paired(
                userName = "Alan",
                friendName = "Kate",
                userEmoji = "\uD83D\uDC35",
                friendEmoji = "\uD83D\uDC36",
            ),
            UserPairState.Invite(
                userName = "Alan",
                userEmoji = "\uD83D\uDC35",
            ),
        )
}
