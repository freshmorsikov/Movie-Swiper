package com.github.freshmorsikov.moviematcher.feature.filter.di

import com.github.freshmorsikov.moviematcher.feature.filter.domain.GetVisibleGenreListUseCase
import com.github.freshmorsikov.moviematcher.feature.filter.domain.SaveRoomGenreFilterUseCase
import com.github.freshmorsikov.moviematcher.feature.filter.presentation.FilterViewModel
import com.github.freshmorsikov.moviematcher.feature.filter.domain.GetGenreListUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val filterFeatureModule = module {
    factory {
        GetVisibleGenreListUseCase()
    }
    factory {
        SaveRoomGenreFilterUseCase(
            getRoomFlowCaseCase = get(),
            roomRepository = get(),
        )
    }
    factory {
        GetGenreListUseCase(movieRepository = get())
    }
    viewModel {
        FilterViewModel(
            getGenreListUseCase = get(),
            getRoomFlowCaseCase = get(),
            getVisibleGenreListUseCase = get(),
            saveRoomGenreFilterUseCase = get(),
        )
    }
}
