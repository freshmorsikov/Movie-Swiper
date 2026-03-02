package com.github.freshmorsikov.moviematcher.shared.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import com.github.freshmorsikov.moviematcher.util.clickableWithoutIndication
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.ic_add
import moviematcher.composeapp.generated.resources.swipe_invite
import moviematcher.composeapp.generated.resources.user_pair_friend
import moviematcher.composeapp.generated.resources.user_pair_you
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter

sealed interface UserPairState {
    val userName: String?
    val userEmoji: String

    data class Paired(
        override val userName: String?,
        override val userEmoji: String,
        val friendName: String?,
        val friendEmoji: String,
    ) : UserPairState

    data class Invite(
        override val userName: String?,
        override val userEmoji: String,
    ) : UserPairState
}

@Composable
fun UserPairCard(
    userPair: UserPairState,
    onInviteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = 14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MovieTheme.colors.surface.main
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
        ) {
            val userName = userPair.userName
                ?: stringResource(Res.string.user_pair_you)
            Spacer(modifier = Modifier.weight(1f))
            EmojiProfileItem(
                name = userName,
                emoji = userPair.userEmoji,
                backgroundColor = MovieTheme.colors.primary,
            )
            HorizontalDivider(
                modifier = Modifier
                    .weight(3f)
                    .padding(
                        top = 24.dp,
                        start = 16.dp,
                        end = 16.dp,
                    ),
                thickness = 2.dp,
                color = MovieTheme.colors.stroke
            )
            when (userPair) {
                is UserPairState.Paired -> {
                    val friendName = userPair.friendName
                        ?: stringResource(Res.string.user_pair_friend)
                    EmojiProfileItem(
                        name = friendName,
                        emoji = userPair.friendEmoji,
                        backgroundColor = MovieTheme.colors.warning,
                    )
                }

                is UserPairState.Invite -> {
                    InviteProfileItem(onInviteClick = onInviteClick)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun EmojiProfileItem(
    name: String,
    emoji: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(shape = CircleShape)
                .background(color = backgroundColor),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = emoji,
                fontSize = 24.sp,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            style = MovieTheme.typography.title16,
            color = MovieTheme.colors.text.main,
        )
    }
}

@Composable
private fun InviteProfileItem(
    onInviteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.clickableWithoutIndication(onClick = onInviteClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(shape = CircleShape)
                .background(color = MovieTheme.colors.surface.variant),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(Res.drawable.ic_add),
                tint = MovieTheme.colors.icon.variant,
                contentDescription = null,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.swipe_invite),
            style = MovieTheme.typography.title16,
            color = MovieTheme.colors.text.variant,
        )
    }
}

@Preview
@Composable
private fun UserPairCardPreview(
    @PreviewParameter(UserPairStatePreviewProvider::class) userPair: UserPairState
) {
    MovieTheme {
        UserPairCard(
            modifier = Modifier.fillMaxWidth(),
            userPair = userPair,
            onInviteClick = {},
        )
    }
}
