package com.github.freshmorsikov.moviematcher.feature.pairing.di

import com.github.freshmorsikov.moviematcher.feature.pairing.domain.JoinPairUseCase
import com.github.freshmorsikov.moviematcher.feature.pairing.presentation.PairingEntryViewModel
import com.github.freshmorsikov.moviematcher.feature.pairing.presentation.PairingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val pairingFeatureModule = module {
    viewModel {
        PairingViewModel(joinPairUseCase = get())
    }
    viewModel { (code: String?) ->
        PairingEntryViewModel(
            code = code,
            userRepository = get(),
        )
    }
    factory {
        JoinPairUseCase(
            getRoomFlowCaseCase = get(),
            userRepository = get(),
        )
    }
}
