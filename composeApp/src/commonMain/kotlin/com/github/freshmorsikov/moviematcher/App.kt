package com.github.freshmorsikov.moviematcher

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.freshmorsikov.moviematcher.api.ApiService
import com.github.freshmorsikov.moviematcher.api.IMAGE_BASE_URL
import com.github.freshmorsikov.moviematcher.util.toRatingFormat
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Scaffold { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                var movieList by remember {
                    mutableStateOf<List<ApiService.MovieResponse>>(emptyList())
                }
                var currentPage by remember {
                    mutableStateOf(1)
                }
                var loading by remember {
                    mutableStateOf(true)
                }
                LaunchedEffect(loading, movieList.size) {
                    println("Test: List size ${movieList.size}")
                    if (!loading && movieList.size < 5) {
                        currentPage++
                    }
                }
                LaunchedEffect(currentPage) {
                    println("Test: Load $currentPage page")
                    loading = true
                    ApiService.getMovieList(page = currentPage)
                        .onSuccess { movieResponseList ->
                            movieList = movieResponseList.results + movieList
                            loading = false
                        }.onFailure { exception ->
                            println(exception.message)
                        }
                }

                movieList.forEach { movie ->
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 8.dp
                                )
                        ) {
                            AsyncImage(
                                model = "$IMAGE_BASE_URL${movie.posterPath}",
                                contentScale = ContentScale.Fit,
                                contentDescription = null,
                            )
                            Text(text = movie.title)
                            if (movie.originalTitle != movie.title) {
                                Text(text = movie.originalTitle)
                            }
                            Text(text = movie.releaseDate)
                            Text(text = "${movie.voteAverage.toRatingFormat()} / 10")
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = spacedBy(16.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF95667)
                        ),
                        onClick = {
                            if (movieList.isNotEmpty()) {
                                movieList -= movieList.last()
                            }
                        }
                    ) {
                        Text("Drop")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00BE64)
                        ),
                        onClick = {
                            if (movieList.isNotEmpty()) {
                                movieList -= movieList.last()
                            }
                        }
                    ) {
                        Text("Like")
                    }
                }
            }
        }
    }
}