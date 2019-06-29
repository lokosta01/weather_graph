package com.example.sokol.weather_test.ui.canvas.draw.data

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

data class Marker(
        var x: Float = 0F,
        var y: Float = 0F,
        var tempGraduc: Int = 0,
        var temp: Double = 0.0,
        var dt_txt: String = "",
        var hour: String = ""
) {
    @SuppressLint("SimpleDateFormat")
    fun getDateFormate(dt_txt: String?): String {
        val mCalendar = Calendar.getInstance()
        mCalendar.time = SimpleDateFormat("yyyy-MM-dd").parse(dt_txt)

        return mCalendar.get(Calendar.DAY_OF_MONTH).toString() + " " +
                mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()).toString()
    }

    @SuppressLint("SimpleDateFormat")
    fun getHour(dt_txt: String?): String {
        val mCalendar = Calendar.getInstance()
        mCalendar.time = SimpleDateFormat("yyyy-MM-dd H:mm").parse(dt_txt)
        return mCalendar.get(Calendar.HOUR_OF_DAY).toString() + ":00"
    }
}