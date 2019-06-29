package com.example.sokol.weather_test.realm

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RealmModule {

    @Provides
    @Singleton
    fun providesRealm(): RealmDao = RealmDao()
}