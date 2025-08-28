package com.github.freshmorsikov.moviematcher.feature.code.di

import com.github.freshmorsikov.moviematcher.feature.code.presentation.CodeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val codeFeatureModule = module {
    viewModel {
        CodeViewModel(getCodeUseCase = get())
    }
}