package com.example.sokol.weather_test.network

import com.example.sokol.weather_test.network.response.WeatherApiResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

//    https://samples.openweathermap.org/data/2.5/find?lat=55.5&lon=37.5&cnt=10&appid=b6907d289e10d714a6e88b30761fae22

    @GET("data/2.5/find?")
    fun getWeather(
            @Query("lat") lat: String,
            @Query("lon") lon: String,
            @Query("cnt") cnt: String = "30"

    ) : Observable<WeatherApiResponse>

    @GET("data/2.5/forecast?")
    fun getDaysWeather(
            @Query("lat") lat: String,
            @Query("lon") lon: String
    ) : Observable<WeatherApiResponse>
}