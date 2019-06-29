package com.example.sokol.weather_test.realm.model

import com.example.sokol.weather_test.ui.canvas.draw.data.Marker
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.*
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class WeatherData() : RealmObject() {

    @PrimaryKey
    @Expose
    @SerializedName("id")
    open var id: Int? = null

    @Expose
    @SerializedName("coord")
    open var coord: Coord? = null

    @Expose
    @SerializedName("name")
    open var name: String? = null


    @Expose
    @SerializedName("dt")
    open var dt: Int? = null


    @Expose
    @SerializedName("wind")
    open var wind: Wind? = null


    @Expose
    @SerializedName("main")
    open var main: Main? = null


    @Expose
    @SerializedName("sys")
    open var sys: Sys? = null

    @SerializedName("rain")
    open var rain: String? = null

    open var snow: String? = null

    @Expose
    @SerializedName("weather")
    open var weather: RealmList<Weather>? = null

    @Expose
    @SerializedName("dt_txt")
    open var dt_txt: String = ""


    fun mapToSend() : Marker? {
            return main?.temp?.let { Marker( 0F,0F,0, it,dt_txt,"") }
        }

}