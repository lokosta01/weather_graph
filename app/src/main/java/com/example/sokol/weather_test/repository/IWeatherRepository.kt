package com.example.sokol.weather_test.repository

import com.example.sokol.weather_test.realm.model.WeatherData
import io.reactivex.Observable

interface IWeatherRepository {
    fun getWeather(lat: String, lon: String, state: Int) : Observable<List<WeatherData>>
}