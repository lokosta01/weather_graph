package com.example.sokol.weather_test.di.module

import android.content.Context
import com.example.sokol.weather_test.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule(val app: App) {

    @Provides
    @Singleton
    fun provideApplication(): Context {
        return app
    }
}