package com.github.freshmorsikov.moviematcher.feature.filter.domain

import com.github.freshmorsikov.moviematcher.feature.movie.domain.model.Genre
import kotlin.test.Test
import kotlin.test.assertEquals

class GetVisibleGenreListUseCaseTest {

    private val useCase = GetVisibleGenreListUseCase()

    @Test
    fun `invoke trims and lowercases search query`() {
        val genre1 = SelectableGenre(
            genre = Genre(id = 1, name = "Action"),
            isSelected = false,
        )
        val genre2 = SelectableGenre(
            genre = Genre(id = 3, name = "Comedy"),
            isSelected = true,
        )
        val genre3 = SelectableGenre(
            genre = Genre(id = 2, name = "Drama"),
            isSelected = false,
        )

        val result = useCase(
            genres = listOf(
                genre1,
                genre2,
                genre3,
            ),
            searchQuery = "  aCT  ",
        )

        assertEquals(listOf(genre1), result)
    }

}
