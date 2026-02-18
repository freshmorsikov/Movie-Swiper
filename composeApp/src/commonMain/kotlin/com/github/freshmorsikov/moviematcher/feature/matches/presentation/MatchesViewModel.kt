package com.github.freshmorsikov.moviematcher.feature.matches.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetMatchedListFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetPairedUserFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.name.domain.GetUserNameUseCase
import com.github.freshmorsikov.moviematcher.shared.domain.GetInviteLinkUseCase
import com.github.freshmorsikov.moviematcher.shared.ui.UserPairState
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class MatchesViewModel(
    getMatchedListFlowUseCase: GetMatchedListFlowUseCase,
    getUserNameUseCase: GetUserNameUseCase,
    getPairedUserFlowUseCase: GetPairedUserFlowUseCase,
    private val getInviteLinkUseCase: GetInviteLinkUseCase,
) : UdfViewModel<MatchesUdf.State, MatchesUdf.Action, MatchesUdf.Event>(
    initState = {
        MatchesUdf.State.Loading
    }
) {

    init {
        viewModelScope.launch {
            val userName = getUserNameUseCase() ?: "You"
            combine(
                getMatchedListFlowUseCase(),
                getPairedUserFlowUseCase(),
            ) { movies, pairedUser ->
                val userPair = if (pairedUser == null) {
                    UserPairState.Invite(
                        userName = userName,
                        userEmoji = getEmojiByName(userName),
                    )
                } else {
                    val friendName = pairedUser.name ?: "Friend"
                    UserPairState.Paired(
                        userName = userName,
                        friendName = friendName,
                        userEmoji = getEmojiByName(userName),
                        friendEmoji = getEmojiByName(friendName),
                    )
                }
                onAction(
                    MatchesUdf.Action.UpdateContent(
                        movies = movies,
                        userPair = userPair,
                    )
                )
            }.launchIn(viewModelScope)
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

            MatchesUdf.Action.InviteClick -> currentState
        }
    }

    override suspend fun handleEffects(action: MatchesUdf.Action) {
        when (action) {
            MatchesUdf.Action.InviteClick -> {
                val inviteLink = getInviteLinkUseCase() ?: return
                sendEvent(MatchesUdf.Event.ShowSharingDialog(inviteLink = inviteLink))
            }

            is MatchesUdf.Action.UpdateContent -> {}
        }
    }


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
