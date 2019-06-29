package com.example.sokol.weather_test.ui.canvas.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin


class CustomView constructor(context: Context, attr: AttributeSet) : View(context, attr) {


    var xpad = (paddingLeft + paddingRight).toFloat()
    val ypad = (paddingTop + paddingBottom).toFloat()


    private var path: Path = Path()
    private var linePaint: Paint = Paint()
    private val startX = 30F
    private val startY = 100F
    private var stopX = 0F
    private var stopY = 0F
    private val radius = 15F

    init {
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        linePaint.isAntiAlias = true

        for (i in 0..361 step 45) {
            stopX = ((radius + 15F) * cos(Math.toRadians(i.toDouble()))).toFloat() + startX
            stopY = ((radius + 15F) * sin(Math.toRadians(i.toDouble()))).toFloat() + startY

            linePaint.strokeWidth = 3F
            linePaint.color = Color.BLACK
            canvas?.drawLine(startX, startY, stopX, stopY, linePaint)
        }
        linePaint.color = Color.YELLOW
        linePaint.style = Paint.Style.FILL
        canvas?.drawCircle(startX, startY, radius, linePaint)



        linePaint.style = Paint.Style.STROKE
        linePaint.color = Color.BLACK
        canvas?.drawCircle(startX, startY, radius, linePaint)


//        val min = Math.min(width, height).toFloat()
    }
}