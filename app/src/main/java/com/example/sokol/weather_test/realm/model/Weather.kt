package com.example.sokol.weather_test.realm.model
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class Weather() : RealmObject() {

    @Expose
    @SerializedName("id")
    open var id: Int? = null
    @Expose
    @SerializedName("main")
    open var main: String? = null

    @Expose
    @SerializedName("description")
    open var description: String? = null
    open var icon: String? = null
}