package com.example.sokol.weather_test.realm.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class Coord(
        @Expose
        @SerializedName("lat")
        open var lat: Double? = null,

        @Expose
        @SerializedName("lon")
        open var lon: Double? = null) : RealmObject()