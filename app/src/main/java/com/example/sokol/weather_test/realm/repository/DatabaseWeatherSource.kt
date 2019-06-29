package com.example.sokol.weather_test.realm.repository

import com.example.sokol.weather_test.realm.RealmDao
import com.example.sokol.weather_test.realm.model.WeatherData
import com.example.sokol.weather_test.repository.source.IWeatherSource
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class DatabaseWeatherSource constructor(val realmDao: RealmDao) : IWeatherSource {

    override fun getWeather(lat: String, lon: String, state: Int): Observable<List<WeatherData>> {
        return realmDao.getWeather()
                .subscribeOn(Schedulers.io())
                .map {
                    it
                }
                .toList()
                .toObservable() as Observable<List<WeatherData>>
    }

    override fun saveWeather(weather: List<WeatherData>) {
        realmDao.saveWeather(weather.toList(
        ))
    }
}