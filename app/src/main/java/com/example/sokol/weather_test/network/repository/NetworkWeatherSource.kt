package com.example.sokol.weather_test.network.repository

import com.example.sokol.weather_test.network.WeatherApiService
import com.example.sokol.weather_test.realm.model.WeatherData
import com.example.sokol.weather_test.repository.source.IWeatherSource
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class NetworkWeatherSource constructor(
        var api: WeatherApiService
) : IWeatherSource {

    override fun getWeather(lat: String, lon: String, state: Int): Observable<List<WeatherData>> {
        return (   when(state){
           1 -> {
               api.getWeather(lat, lon)   //где берем координаты
                       .map { t ->
                           t.list ?: emptyList()
                       }
                       .subscribeOn(Schedulers.io())
           }
           else -> {
             api.getDaysWeather(lat, lon)
                       .map { t ->
                           t.list ?: emptyList()
                       }
                       .subscribeOn(Schedulers.io())
           }
       })
    }

    override fun saveWeather(weather: List<WeatherData>) {
        throw IllegalStateException("not implemented")
    }

}