package com.example.sokol.weather_test.ui.canvas.draw.data

data class Graph(
        val padding: Int = 10, //отступ
        val scalesWidth: Int = 5,//ширина шкалы
        var zeroY: Int = 0,
        val chartHeight: Int = 400,
        var paddingLeft: Int = 40,
        val paddingTop: Int = 40,
        var state: Boolean = true,
        var step: Int = 1,
        var radius: Float = 5F,
        var innerRadius: Float = 3F,

        var markers: List<Marker> = mutableListOf(),
        var list: MutableList<CordMarker> = mutableListOf()
)
