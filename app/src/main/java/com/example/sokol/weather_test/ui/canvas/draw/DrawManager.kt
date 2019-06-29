package com.example.sokol.weather_test.ui.canvas.draw

import android.content.Context
import android.graphics.Canvas
import com.example.sokol.weather_test.ui.canvas.animation.data.AnimationValue
import com.example.sokol.weather_test.ui.canvas.draw.data.Graph

class DrawManager (context: Context){

    private val graph = Graph()
    private val controller = DrawController(context, graph)

    fun graph(): Graph {
        return graph
    }

    fun draw(canvas: Canvas) {
        controller.draw(canvas)
    }

    fun drawAnimation(canvas: Canvas){
        controller.drawAnimation(canvas)
    }

    fun updateValue(value: AnimationValue) {
        controller.updateValue(value)
    }
}