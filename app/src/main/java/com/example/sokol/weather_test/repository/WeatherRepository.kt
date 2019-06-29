package com.example.sokol.weather_test.repository

import com.example.sokol.weather_test.realm.model.WeatherData
import com.example.sokol.weather_test.repository.source.IWeatherSource
import com.example.sokol.weather_test.storage.repository.Repository
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Named

class WeatherRepository constructor(@Named(Repository.DATABASE) var dbSource: IWeatherSource,
                                    @Named(Repository.NETWORK) var networkSource: IWeatherSource) : IWeatherRepository {

    override fun getWeather(lat: String, lon: String, state: Int): Observable<List<WeatherData>> {
        return Observable.concatArray(
                networkSource.getWeather(lat, lon, state),
                dbSource.getWeather(lat, lon, state)

                        .doOnNext{dbSource.saveWeather(it)}
        ).debounce(500,TimeUnit.MILLISECONDS)
    }
}