package com.example.sokol.weather_test.ui.canvas.draw

import android.content.Context
import android.graphics.*
import com.example.sokol.weather_test.R
import com.example.sokol.weather_test.ui.canvas.animation.AnimationManager
import com.example.sokol.weather_test.ui.canvas.animation.data.AnimationValue
import com.example.sokol.weather_test.ui.canvas.draw.data.Graph
import com.example.sokol.weather_test.ui.canvas.draw.data.Marker

@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DrawController(context: Context, private val graph: Graph) {

    companion object {
        const val STROKE_WIDTH = 3F
    }

    private var value = AnimationValue()

    var gradientPaint: Paint? = null
    var strokePain: Paint? = null
    var radiusPain: Paint? = null
    var dottedPaint: Paint? = null
    var textPaint: Paint? = null
    var hourPaint: Paint? = null
    var rectPaint: Paint? = null
    var path: Path? = null
    var rect: Rect? = null

    private val paddingLeft: Int = graph.paddingLeft
    private val paddingTop: Int = graph.paddingTop
    private val padding: Int = graph.padding
    private var radius: Float = graph.radius
    private var innerRadius = graph.innerRadius
    private var step: Int = graph.step
    private var formatString: String
    private var colorResources: Int

    init {
        path = Path()

        gradientPaint = Paint()
        strokePain = Paint()
        radiusPain = Paint()
        dottedPaint = Paint()
        textPaint = Paint()
        hourPaint = Paint()
        textPaint?.textSize = 20F
        hourPaint?.textSize = 10F
        rectPaint = Paint()
        rect = Rect()

        //сетка
        dottedPaint?.style = Paint.Style.STROKE
        dottedPaint?.color = Color.GRAY
        dottedPaint?.strokeWidth = STROKE_WIDTH
        dottedPaint?.pathEffect = DashPathEffect(floatArrayOf(radius, radius), 0F)

        formatString = context.getString(R.string.temperature_format)
        colorResources = context.resources.getColor(R.color.colorPrimary)

        //маркеры
        strokePain?.color = Color.RED
        strokePain?.strokeWidth = STROKE_WIDTH
        radiusPain?.color = Color.WHITE
    }

    fun updateValue(value: AnimationValue) {
        this.value = value
    }

    fun draw(canvas: Canvas) {
        drawLineAndMarkers(canvas)
        drawFrame(canvas)
    }
    fun drawAnimation(canvas: Canvas){
        drawFrame(canvas)
        drawGraph(canvas)
    }

    private fun drawFrame(canvas: Canvas) {
        drawGradient(canvas)
        drawGuidelines(canvas)
        drawGraduations(canvas)
        drawWeeks(canvas)
    }

    private fun drawGraph(canvas: Canvas) {
        val runningAnimationPosition = if (value != null) value.runningAnimationPosition else AnimationManager.VALUE_NONE

        for (i in 0 until runningAnimationPosition) {
            drawGraph(canvas, i, false)
        }

        if (runningAnimationPosition > AnimationManager.VALUE_NONE) {
            drawGraph(canvas, runningAnimationPosition, true)
        }
    }

    private fun drawGraph(canvas: Canvas, position: Int, isAnimation: Boolean) {
        val list = graph.list
        if (position > list.lastIndex - 1) {
            return
        }

        val listData = list[position]
        val startX = listData.startX
        val startY = listData.startY

        val stopX: Float
        val stopY: Float
        val alpha: Int

        if (isAnimation) {
            stopX = value.x
            stopY = value.y
            alpha = value.alpha

        } else {
            stopX = listData.stopX
            stopY = listData.stopY
            alpha = AnimationManager.ALPHA_END
        }

        drawGraph(canvas, startX, startY, stopX, stopY, alpha, position)
    }

    private fun drawGraph(canvas: Canvas, startX: Float, startY: Float, stopX: Float, stopY: Float, alpha: Int, position: Int) {

        canvas.drawLine(startX, startY, stopX, stopY, strokePain)

        if (position > 0) {
            strokePain?.alpha = alpha
            canvas.drawCircle(startX, startY, radius, strokePain)
            canvas.drawCircle(startX, startY, innerRadius, radiusPain)
        }
    }


    private fun drawGradient(canvas: Canvas) {
        //градиент
        val colors = intArrayOf(colorResources, Color.TRANSPARENT)
        val gradient = LinearGradient(
                0F, paddingTop.toFloat(), 0F, graph.zeroY.toFloat(), colors, null, Shader.TileMode.CLAMP
        )
        gradientPaint?.style = Paint.Style.FILL
        gradientPaint?.shader = gradient

        val markers = graph.markers

        path?.reset()
        path?.moveTo(paddingLeft.toFloat(), graph.zeroY.toFloat())

        step = if (!graph.state) 1 else 3

        for (marker in 0..markers!!.lastIndex step step) {
            path?.lineTo(markers[marker].x, markers[marker].y)
        }
        // close the path
        markers.last().x.let { path?.lineTo(it, graph.zeroY.toFloat()) }
        path?.lineTo(paddingLeft.toFloat(), graph.zeroY.toFloat())

        canvas.drawPath(path, gradientPaint)
    }

    //    Нарисовать сетку
    private fun drawGuidelines(canvas: Canvas) {
        val markers = graph.markers

        for (i in 0..(markers.lastIndex - 1)) {

            val marker = markers[i]
            val markerI = markers[i + 1]
            if (marker.dt_txt != markerI.dt_txt) {

                path?.reset()
                marker.x.let {
                    path?.moveTo(it, paddingTop.toFloat())
                    path?.lineTo(it, graph.zeroY.toFloat())
                }
                canvas.drawPath(path, dottedPaint)
            }
        }
    }

    private fun drawLineAndMarkers(canvas: Canvas) {
        val markers = graph.markers
        var previousMarker: Marker? = null

        step = if (!graph.state) 1 else 3

        for (i in 0..(markers.lastIndex) step step) {
            if (previousMarker != null) {

                // draw the line
                val p1 = previousMarker
                val p2 = markers[i]

                p2.let { canvas.drawLine(p1.x, p1.y, it.x, it.y, strokePain) }
            }
            previousMarker = markers[i]
            // draw the marker

            markers[i].let {
                canvas.drawCircle(
                        it.x,
                        it.y,
                        radius,
                        strokePain
                )
                canvas.drawCircle(
                        it.x,
                        it.y,
                        innerRadius,
                        radiusPain
                )
            }
        }
    }

    //нарисовать числовые маркеры справа
    private fun drawGraduations(canvas: Canvas) {
        val markers = graph.markers
        val x = markers.last().x + padding
        for (marker in markers.iterator()) {

            val y = marker.y
            val value = String.format(formatString, marker.tempGraduc.toString())
            canvas.drawText(value, x, y, textPaint)
        }
    }

    //    Нарисовать кнопки дней
    private fun drawWeeks(canvas: Canvas) {
        val markers = graph.markers
        for (i in 0..(markers.lastIndex - 1)) {

            val marker = markers[i]
            val markerI = markers[i+1]
            if (marker.dt_txt != markerI.dt_txt) {

                val left = marker.x.plus(padding)
                val bottom =  rect?.height()?.let { graph.zeroY + it }

                left.let { bottom?.toFloat()?.let { it1 -> canvas.drawText(markerI.dt_txt, it, it1, textPaint) } }
            }
        }
        if (!graph.state) {
            drawHours(canvas)
        }
    }

    //нарисовать шкалу времени
    private fun drawHours(canvas: Canvas) {
        val markers = graph.markers
        for (i in 0..markers.lastIndex - 2 step 2) {
            val bottom = graph.zeroY + 2 * padding

            canvas.drawText(markers[i].hour, markers[i].x, bottom.toFloat(), hourPaint)
        }
    }
}