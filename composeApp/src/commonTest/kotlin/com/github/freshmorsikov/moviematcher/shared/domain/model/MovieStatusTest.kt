package com.github.freshmorsikov.moviematcher.shared.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class MovieStatusTest {

    @Test
    fun `selecting current status resets to undefined`() {
        assertEquals(
            MovieStatus.Undefined,
            MovieStatus.Liked.toggleSelection(currentStatus = MovieStatus.Liked),
        )
    }

    @Test
    fun `selecting a different status replaces current status`() {
        assertEquals(
            MovieStatus.Disliked,
            MovieStatus.Disliked.toggleSelection(currentStatus = MovieStatus.Liked),
        )
    }

    @Test
    fun `selecting status from undefined sets status`() {
        assertEquals(
            MovieStatus.Liked,
            MovieStatus.Liked.toggleSelection(currentStatus = MovieStatus.Undefined),
        )
    }

}
