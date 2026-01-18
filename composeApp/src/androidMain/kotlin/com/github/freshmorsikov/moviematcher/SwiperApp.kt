package com.github.freshmorsikov.moviematcher

import android.app.Application
import android.content.Context
import com.github.freshmorsikov.moviematcher.core.di.initKoin
import org.koin.dsl.module

class SwiperApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(platformModule = androidModule())
        Start.startApp()
    }

    private fun androidModule() = module {
        single<Context> { applicationContext }
    }
}