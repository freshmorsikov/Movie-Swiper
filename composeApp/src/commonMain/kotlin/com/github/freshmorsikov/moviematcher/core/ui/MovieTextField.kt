package com.github.freshmorsikov.moviematcher.core.ui

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun MovieTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIconRes: DrawableResource? = null,
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        textStyle = MovieTheme.typography.body16,
        placeholder = {
            Text(
                text = placeholder,
                color = MovieTheme.colors.text.variant,
                style = MovieTheme.typography.body16,
            )
        },
        leadingIcon = {
            leadingIconRes?.let { res ->
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(resource = res),
                    tint = MovieTheme.colors.icon.variant,
                    contentDescription = null
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MovieTheme.colors.primary.copy(alpha = 0.2f),
            unfocusedContainerColor = MovieTheme.colors.primary.copy(alpha = 0.2f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
    )
}