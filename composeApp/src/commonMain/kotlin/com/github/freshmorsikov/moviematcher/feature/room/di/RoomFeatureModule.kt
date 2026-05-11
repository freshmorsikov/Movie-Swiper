package com.github.freshmorsikov.moviematcher.feature.room.di

import com.github.freshmorsikov.moviematcher.feature.room.data.RoomRemoteDataSource
import org.koin.dsl.module

val roomFeatureModule = module {
    single {
        RoomRemoteDataSource(
            supabaseClient = get(),
        )
    }
}
