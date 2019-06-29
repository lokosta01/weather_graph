package com.example.sokol.weather_test.repository.source

import com.example.sokol.weather_test.realm.model.WeatherData
import io.reactivex.Observable

interface IWeatherSource {

    fun getWeather(lat: String, lon: String, state: Int) : Observable<List<WeatherData>>

    fun saveWeather(weather: List<WeatherData>)
}