package com.github.freshmorsikov.moviematcher

import android.app.Application
import android.content.Context
import com.github.freshmorsikov.moviematcher.core.di.initKoin
import org.koin.dsl.module

class SwiperApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(contextModule = androidContextModule())
    }

    private fun androidContextModule() = module {
        single<Context> { applicationContext }
    }
}