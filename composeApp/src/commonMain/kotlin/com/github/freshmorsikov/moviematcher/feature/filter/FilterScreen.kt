package com.github.freshmorsikov.moviematcher.feature.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.github.freshmorsikov.moviematcher.core.ui.MovieBackButton
import com.github.freshmorsikov.moviematcher.core.ui.MovieScaffold
import com.github.freshmorsikov.moviematcher.core.ui.MovieTextField
import com.github.freshmorsikov.moviematcher.core.ui.OutlinedMovieButton
import com.github.freshmorsikov.moviematcher.core.ui.PrimaryMovieButton
import com.github.freshmorsikov.moviematcher.core.ui.Shimmer
import com.github.freshmorsikov.moviematcher.core.ui.theme.MovieTheme
import com.github.freshmorsikov.moviematcher.feature.filter.domain.SelectableGenre
import com.github.freshmorsikov.moviematcher.feature.filter.presentation.FilterUdf
import com.github.freshmorsikov.moviematcher.feature.filter.presentation.FilterViewModel
import com.github.freshmorsikov.moviematcher.feature.movie.domain.model.Genre
import com.github.freshmorsikov.moviematcher.util.SubscribeOnEvents
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.filter_apply
import moviematcher.composeapp.generated.resources.filter_cancel
import moviematcher.composeapp.generated.resources.filter_no_genres_available
import moviematcher.composeapp.generated.resources.filter_no_genres_found
import moviematcher.composeapp.generated.resources.filter_search_genres
import moviematcher.composeapp.generated.resources.filter_select_genres
import moviematcher.composeapp.generated.resources.ic_search
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FilterScreen(
    navController: NavController,
    viewModel: FilterViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SubscribeOnEvents(viewModel.event) { event ->
        when (event) {
            FilterUdf.Event.CloseScreen -> {
                navController.popBackStack()
            }
        }
    }

    FilterScreenContent(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun FilterScreenContent(
    state: FilterUdf.State,
    onAction: (FilterUdf.Action) -> Unit,
) {
    MovieScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding() + 8.dp),
        ) {
            FilterHeader(
                searchQuery = (state as? FilterUdf.State.Success)?.searchQuery.orEmpty(),
                onSearchQueryChange = { value ->
                    onAction(FilterUdf.Action.HandleSearchQuery(searchQuery = value))
                },
                onBackClick = {
                    onAction(FilterUdf.Action.BackClick)
                },
            )
            Box(modifier = Modifier.weight(1f)) {
                when (state) {
                    is FilterUdf.State.Loading -> {
                        FilterLoadingState()
                    }

                    is FilterUdf.State.Success -> {
                        when {
                            state.currentGenres.isEmpty() -> {
                                FilterEmptyState(
                                    text = stringResource(Res.string.filter_no_genres_available)
                                )
                            }

                            state.visibleGenres.isEmpty() -> {
                                FilterEmptyState(
                                    text = stringResource(Res.string.filter_no_genres_found)
                                )
                            }

                            else -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(16.dp),
                                ) {
                                    items(
                                        items = state.visibleGenres,
                                        key = { genre -> genre.genre.id },
                                    ) { genre ->
                                        GenreItem(
                                            genre = genre,
                                            enabled = !state.isSaving,
                                            onToggleSelection = {
                                                onAction(FilterUdf.Action.ToggleGenre(genreId = genre.genre.id))
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding() + 16.dp),
            ) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MovieTheme.colors.stroke,
                )
                Row(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp,
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedMovieButton(
                        modifier = Modifier.weight(1f),
                        text = stringResource(Res.string.filter_cancel),
                        onClick = {
                            onAction(FilterUdf.Action.CancelClick)
                        },
                        color = MovieTheme.colors.primary,
                        enabled = state !is FilterUdf.State.Success || !state.isSaving,
                    )
                    PrimaryMovieButton(
                        modifier = Modifier.weight(1f),
                        text = stringResource(Res.string.filter_apply),
                        onClick = {
                            onAction(FilterUdf.Action.ApplyClick)
                        },
                        enabled = (state as? FilterUdf.State.Success)?.isApplyEnabled == true,
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            MovieBackButton(onClick = onBackClick)
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.filter_select_genres),
                color = MovieTheme.colors.text.main,
                style = MovieTheme.typography.title20,
                textAlign = TextAlign.Center,
            )
        }
        MovieTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = stringResource(Res.string.filter_search_genres),
            leadingIconRes = Res.drawable.ic_search,
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
            thickness = 1.dp,
            color = MovieTheme.colors.stroke,
        )
    }
}

@Composable
private fun GenreItem(
    genre: SelectableGenre,
    enabled: Boolean,
    onToggleSelection: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MovieTheme.colors.surface.main)
            .clickable(
                enabled = enabled,
                onClick = onToggleSelection,
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = genre.isSelected,
            onCheckedChange = {
                onToggleSelection()
            },
            colors = CheckboxDefaults.colors(
                checkedColor = MovieTheme.colors.primary,
                uncheckedColor = MovieTheme.colors.primaryDisabled,
                checkmarkColor = MovieTheme.colors.text.onAccent,
            ),
        )
        Text(
            modifier = Modifier
                .padding(start = 4.dp)
                .weight(1f),
            text = genre.genre.name,
            style = MovieTheme.typography.body16,
            color = MovieTheme.colors.text.main,
        )
    }
}

@Composable
private fun FilterLoadingState() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        repeat(6) {
            Shimmer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@Composable
private fun FilterEmptyState(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MovieTheme.typography.title16,
            color = MovieTheme.colors.text.variant,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun FilterScreenPreview() {
    val initialGenres = listOf(
        SelectableGenre(
            genre = Genre(id = 1, name = "Action"),
            isSelected = true,
        ),
        SelectableGenre(
            genre = Genre(id = 2, name = "Drama"),
            isSelected = false,
        ),
        SelectableGenre(
            genre = Genre(id = 3, name = "Comedy"),
            isSelected = false,
        ),
    )
    val currentGenres = listOf(
        SelectableGenre(
            genre = Genre(id = 1, name = "Action"),
            isSelected = true,
        ),
        SelectableGenre(
            genre = Genre(id = 2, name = "Drama"),
            isSelected = false,
        ),
        SelectableGenre(
            genre = Genre(id = 3, name = "Comedy"),
            isSelected = true,
        ),
    )
    MovieTheme {
        FilterScreenContent(
            state = FilterUdf.State.Success(
                isSaving = false,
                searchQuery = "",
                initialGenres = initialGenres,
                currentGenres = currentGenres,
                visibleGenres = currentGenres,
                isApplyEnabled = true,
            ),
            onAction = {},
        )
    }
}
