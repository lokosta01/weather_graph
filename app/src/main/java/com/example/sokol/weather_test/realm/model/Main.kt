package com.example.sokol.weather_test.realm.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class Main(

        @Expose
        @SerializedName("temp")
        open var temp: Double = 0.0,

        open var pressure: Int = 0,

        @Expose
        @SerializedName("humidity")
        open var humidity: Int = 0,


        open var tempMin: Int = 0,


        open var tempMax: Int = 0
) : RealmObject()