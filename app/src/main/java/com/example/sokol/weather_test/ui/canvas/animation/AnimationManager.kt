package com.example.sokol.weather_test.ui.canvas.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import com.example.sokol.weather_test.ui.canvas.animation.data.AnimationValue
import com.example.sokol.weather_test.ui.canvas.draw.data.CordMarker
import com.example.sokol.weather_test.ui.canvas.draw.data.Graph

class AnimationManager (private var listener: AnimationListener, private var graph: Graph){

    companion object {
        const val PROPERTY_X = "PROPERTY_X"
        const val PROPERTY_Y = "PROPERTY_Y"
        const val PROPERTY_ALPHA = "PROPERTY_ALPHA"

        const val VALUE_NONE = -1
        const val ALPHA_START = 0
        const val ALPHA_END = 255
        const val ANIMATION_DURATION = 250L
    }

    private var animatorSet: AnimatorSet = AnimatorSet()
    private var lastValue: AnimationValue = AnimationValue()

    interface AnimationListener : Animation.AnimationListener {
       fun onAnimationUpdated(value: AnimationValue)
   }

    fun animate(){
        animatorSet.playSequentially(createAnimatorList())
        animatorSet.start()
    }

    private fun createAnimatorList(): List<Animator> {
        val list  = graph.list
        val animatorList = arrayListOf<Animator>()

        for (cord in list.iterator()) {
            animatorList.add(createAnimator(cord))
        }
        return animatorList
    }


    private fun createAnimator(cord: CordMarker): ValueAnimator {

        val propertyX = PropertyValuesHolder.ofFloat(PROPERTY_X, cord.startX, cord.stopX)
        val propertyY = PropertyValuesHolder.ofFloat(PROPERTY_Y, cord.startY, cord.stopY)
        val propertyAlpha = PropertyValuesHolder.ofInt(PROPERTY_ALPHA, ALPHA_START, ALPHA_END)

        val animator = ValueAnimator()
        animator.setValues(propertyX, propertyY, propertyAlpha)
        animator.duration = ANIMATION_DURATION
        animator.interpolator = AccelerateDecelerateInterpolator()

        animator.addUpdateListener { animation ->
            //здесь должно меняться значение
            onAnimationUpdate(animation)
        }
        return animator
    }

    private fun onAnimationUpdate(valueAnimator: ValueAnimator?) {
        if (valueAnimator == null) {
            return
        }

        val x = valueAnimator.getAnimatedValue(PROPERTY_X) as Float
        val y = valueAnimator.getAnimatedValue(PROPERTY_Y) as Float
        val alpha = valueAnimator.getAnimatedValue(PROPERTY_ALPHA) as Int
        val runningAnimationPosition = getRunningAnimationPosition()

        val value = AnimationValue()
        value.x = x
        value.y = y
        value.alpha = adjustAlpha(runningAnimationPosition, alpha)
        value.runningAnimationPosition = runningAnimationPosition

        listener.onAnimationUpdated(value)
        lastValue = value
    }

    private fun getRunningAnimationPosition(): Int {
        val childAnimations = animatorSet.childAnimations
        for (i in childAnimations.indices) {
            val animator = childAnimations[i]
            if (animator.isRunning) {
                return i
            }
        }

        return VALUE_NONE
    }

    private fun adjustAlpha(runningPos: Int, alpha: Int): Int {

        val isPositionIncreased = runningPos > lastValue.runningAnimationPosition
        val isAlphaIncreased = alpha > lastValue.alpha

        return if (!isPositionIncreased && !isAlphaIncreased) {
            lastValue.alpha
        } else {
            alpha
        }
    }
}
