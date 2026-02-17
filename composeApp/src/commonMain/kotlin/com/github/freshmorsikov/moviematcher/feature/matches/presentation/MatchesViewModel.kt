package com.github.freshmorsikov.moviematcher.feature.matches.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetMatchedListFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.name.domain.GetUserNameUseCase
import com.github.freshmorsikov.moviematcher.shared.ui.UserPairState
import kotlinx.coroutines.launch

class MatchesViewModel(
    getMatchedListFlowUseCase: GetMatchedListFlowUseCase,
    getUserNameUseCase: GetUserNameUseCase,
) : UdfViewModel<MatchesUdf.State, MatchesUdf.Action, MatchesUdf.Event>(
    initState = {
        MatchesUdf.State.Loading
    }
) {

    init {
        viewModelScope.launch {
            val userName = getUserNameUseCase() ?: "You"
            val friendName = "Kate"
            getMatchedListFlowUseCase().collect { movies ->
                val userPairState = UserPairState.Paired(
                    userName = userName,
                    friendName = friendName,
                    userEmoji = getEmojiByName(userName),
                    friendEmoji = getEmojiByName(friendName),
                )
                onAction(
                    MatchesUdf.Action.UpdateContent(
                        movies = movies,
                        userPair = userPairState,
                    )
                )
            }
        }
    }

    override fun reduce(action: MatchesUdf.Action): MatchesUdf.State {
        return when (action) {
            is MatchesUdf.Action.UpdateContent -> {
                if (action.movies.isEmpty()) {
                    MatchesUdf.State.Empty(
                        userPair = action.userPair
                    )
                } else {
                    MatchesUdf.State.Data(
                        movies = action.movies,
                        userPair = action.userPair,
                    )
                }
            }
        }
    }

    override suspend fun handleEffects(action: MatchesUdf.Action) {}


    private fun getEmojiByName(name: String): String {
        return EMOJIS[name.hashCode() % EMOJIS.size]
    }

    companion object {
        private val EMOJIS = listOf(
            "\uD83D\uDC35",
            "\uD83D\uDC36",
            "\uD83D\uDC39",
            "\uD83D\uDC30",
            "\uD83E\uDD8A",
            "\uD83D\uDC3C",
            "\uD83E\uDD81",
            "\uD83D\uDC2F",
        )
    }

}
