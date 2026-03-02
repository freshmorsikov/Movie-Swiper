package com.github.freshmorsikov.moviematcher.feature.pairing.di

import com.github.freshmorsikov.moviematcher.feature.pairing.data.PairingRepository
import com.github.freshmorsikov.moviematcher.feature.pairing.domain.ClearPairingCodeUseCase
import com.github.freshmorsikov.moviematcher.feature.pairing.domain.GetPairingCodeUseCase
import com.github.freshmorsikov.moviematcher.feature.pairing.domain.JoinPairUseCase
import com.github.freshmorsikov.moviematcher.feature.pairing.domain.SavePairingCodeUseCase
import com.github.freshmorsikov.moviematcher.feature.pairing.presentation.EntryViewModel
import com.github.freshmorsikov.moviematcher.feature.pairing.presentation.PairingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val pairingFeatureModule = module {
    single {
        PairingRepository()
    }
    viewModel {
        PairingViewModel(
            getPairingCodeUseCase = get(),
            clearPairingCodeUseCase = get(),
            joinPairUseCase = get(),
        )
    }
    viewModel { (code: String?) ->
        EntryViewModel(
            code = code,
            checkConnectivityUseCase = get(),
            getUserNameUseCase = get(),
            getPairingCodeUseCase = get(),
            savePairingCodeUseCase = get(),
        )
    }
    factory {
        JoinPairUseCase(
            getRoomFlowCaseCase = get(),
            userRepository = get(),
        )
    }
    factory {
        GetPairingCodeUseCase(pairingRepository = get())
    }
    factory {
        SavePairingCodeUseCase(pairingRepository = get())
    }
    factory {
        ClearPairingCodeUseCase(pairingRepository = get())
    }
}
