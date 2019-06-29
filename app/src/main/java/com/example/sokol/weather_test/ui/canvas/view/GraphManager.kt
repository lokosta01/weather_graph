package com.example.sokol.weather_test.ui.canvas.view

import android.content.Context
import android.view.animation.Animation
import com.example.sokol.weather_test.ui.canvas.animation.AnimationManager
import com.example.sokol.weather_test.ui.canvas.animation.data.AnimationValue
import com.example.sokol.weather_test.ui.canvas.draw.DrawManager
import com.example.sokol.weather_test.ui.canvas.draw.data.Graph

class GraphManager(context: Context, private val listener: AnimationListener) : AnimationManager.AnimationListener {

    private val drawManager = DrawManager(context)
    private val animationManager = AnimationManager(this, drawManager.graph())

    fun graph(): Graph {
        return drawManager.graph()
    }

    fun drawer(): DrawManager {
        return drawManager
    }

    fun animate() {
        if (drawManager.graph().list.isNotEmpty()) {
            animationManager.animate()
        }
    }

    override fun onAnimationRepeat(animation: Animation?) {}

    override fun onAnimationEnd(animation: Animation?) {}

    override fun onAnimationStart(animation: Animation?) {}

    override fun onAnimationUpdated(value: AnimationValue) {
        drawManager.updateValue(value)
        listener.onAnimationUpdated()
    }

    interface AnimationListener {
        fun onAnimationUpdated()
    }
}