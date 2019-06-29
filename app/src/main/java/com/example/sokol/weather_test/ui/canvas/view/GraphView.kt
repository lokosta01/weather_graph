package com.example.sokol.weather_test.ui.canvas.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Scroller
import com.example.sokol.weather_test.ui.canvas.draw.data.CordMarker
import com.example.sokol.weather_test.ui.canvas.draw.data.Graph
import com.example.sokol.weather_test.ui.canvas.draw.data.Marker

@SuppressLint("ViewConstructor")
class GraphView constructor(context: Context, attr: AttributeSet) : View(context, attr),
        GraphManager.AnimationListener {

    companion object {
        const val MIN_ZOOM: Float = 1f
        const val MAX_ZOOM: Float = 2f
    }

    private var graphManager =  GraphManager(context, this)
    val graph: Graph = graphManager.graph()
    private var detector: ScaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private var listener: GestureDetector = GestureDetector(context, GestureListener(this))
    private var scroll: Scroller = Scroller(context)
    private var scaleFactor: Float = 1f


    var state = graph.state
    var detectorState: Boolean = false

    var step: Int = graph.step
    private var zeroY: Int = 0
    private var pxPerUnit: Int = 0
    private var markers: List<Marker>? = null
    private var copy: List<Marker>? = null
    var list = mutableListOf<CordMarker>()

    fun setView(marker: List<Marker>?) {
        this.markers = marker
        marker?.let {  graph.markers = it }

        calcPositions(graph.markers)

        postInvalidate()

        post {
            graphManager.animate()
        }
    }

    private fun calcPositions(markers: List<Marker>) {

        val max = markers.maxBy {
            it.temp
        }?.temp?.minus(273.15)?.toInt() ?: 0
        val min = markers.minBy {
            it.temp
        }?.temp?.minus(273.15)?.toInt() ?: 0


        pxPerUnit = graph.chartHeight / (max - min)
        zeroY = (max * pxPerUnit + paddingTop)

        graph.zeroY = zeroY

        val step = (width - 2 * graph.paddingLeft - graph.scalesWidth) / (markers.size - 1)  //размер шага по горизонтали между маркерами
        for ((i, marker) in markers.withIndex()) {
            val x = (step * i + paddingLeft).toFloat()
            val y = (zeroY - marker.temp.minus(273.15).toInt() * pxPerUnit).toFloat()
            marker.x = x
            marker.y = y
            marker.tempGraduc = marker.temp.minus(273.15).toInt()
            marker.hour = marker.getHour(marker.dt_txt)
            marker.dt_txt = marker.getDateFormate(marker.dt_txt)
        }
        copy = markers.map {
            it.copy()
        }

        calcCord()
    }

    private fun calcCord() {
        markers = graph.markers
        var previousMarker: Marker? = null

        step = if (!state) 1 else 3

        for (i in 0..(markers.orEmpty().size) step step) {
            if (previousMarker != null) {

                // draw the line
                val p1 = previousMarker
                val p2 = markers?.get(i)

            p2?.let { list.add(CordMarker(p1.x, it.x, p1.y, it.y)) }
            }
            graph.list = list

            previousMarker = markers?.get(i)
        }
    }

    override fun onAnimationUpdated() {
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            if (markers == null)
                return

            scroll.let { it1 ->
                if (it1.computeScrollOffset()) {
                    scrollTo(
                            it1.currX, it1.currY)
                }
            }

            if(detectorState) graphManager.drawer().draw(it) else graphManager.drawer().drawAnimation(it)
        }
    }

    @SuppressLint("ClickableViewAccessibility", "NewApi", "ResourceAsColor")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        detector.onTouchEvent(event)
        listener.onTouchEvent(event)

        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            Log.e("TAG", detector?.scaleFactor.toString())
            detectorState = true
            scaleFactor *= detector?.scaleFactor ?: 1F
            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM))

            graph.state = state == scaleFactor < 2

            if (detector?.scaleFactor!! < 1 && copy?.last()!!.x <= markers?.last()?.x ?: 0F) {

                markers?.withIndex()?.forEach { (i, marker) ->
                    marker.x = marker.x - scaleFactor * i
                }

            } else {
                markers?.withIndex()?.forEach { (i, marker) ->
                    marker.x = marker.x + i * scaleFactor
                }
            }

            invalidate()

            return true
        }
    }

    private inner class GestureListener constructor(var graphView: GraphView) : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent?): Boolean {

            scroll.let {
                if (!it.isFinished) {
                    it.forceFinished(true)
                }
                return true
            }
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            scroll.fling(scrollX, scrollY, -velocityX.toInt(),
                    -velocityY.toInt(), 0, 100, 0, 0)
            invalidate()
            return true
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            graphView.scrollBy(distanceX.toInt(), 0)

            return true
        }
    }


}