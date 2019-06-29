package com.example.sokol.weather_test.di.module

import com.example.sokol.weather_test.network.WeatherApiService
import com.example.sokol.weather_test.network.repository.NetworkWeatherSource
import com.example.sokol.weather_test.realm.RealmDao
import com.example.sokol.weather_test.realm.repository.DatabaseWeatherSource
import com.example.sokol.weather_test.repository.IWeatherRepository
import com.example.sokol.weather_test.repository.WeatherRepository
import com.example.sokol.weather_test.repository.source.IWeatherSource
import com.example.sokol.weather_test.storage.repository.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class RepositoryModule {

    @Provides
    @Named(Repository.DATABASE)
    fun provideDatabaseWeatherSource(realmDao: RealmDao): IWeatherSource {
        return DatabaseWeatherSource(realmDao)
    }

    @Provides
    @Named(Repository.NETWORK)
    fun providerNetworkWeatherSource(apiService: WeatherApiService) : IWeatherSource {
        return NetworkWeatherSource(apiService)
    }

    @Provides
    fun provideWeatherRepository(@Named(Repository.DATABASE) dbSource: IWeatherSource,
                                 @Named(Repository.NETWORK) networkSource : IWeatherSource)
            : IWeatherRepository {
        return WeatherRepository(dbSource, networkSource)
    }
}