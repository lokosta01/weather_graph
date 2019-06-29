package com.example.sokol.weather_test.realm.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class Wind(
        @Expose
        @SerializedName("speed")
        open var speed: Double? = null,

        open var deg: Int? = null) : RealmObject()