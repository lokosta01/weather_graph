package com.example.sokol.weather_test.realm.model

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmObject

open class Clouds() : RealmObject(), Parcelable {
    var all: Int? = null

    constructor(parcel: Parcel) : this() {
        all = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(all)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Clouds> {
        override fun createFromParcel(parcel: Parcel): Clouds {
            return Clouds(parcel)
        }

        override fun newArray(size: Int): Array<Clouds?> {
            return arrayOfNulls(size)
        }
    }
}