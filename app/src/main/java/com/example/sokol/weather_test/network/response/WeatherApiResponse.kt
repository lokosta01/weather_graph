package com.example.sokol.weather_test.network.response

import com.example.sokol.weather_test.realm.model.WeatherData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherApiResponse(

        @Expose
        @SerializedName("message")
        val message: String?,

        @Expose
        @SerializedName("cod")
        val cod: Int = 0,


        @Expose
        @SerializedName("count")
        val count: Int,


        @Expose
        @SerializedName("list")
        var list: List<WeatherData>? = null
)


