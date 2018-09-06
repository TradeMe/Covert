package nz.co.trademe.covert.canvas

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.FloatRange
import android.support.annotation.Px
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnEnd
import nz.co.trademe.covert.model.*

/**
 * Lightweight canvas drawable for handling interactive Icon lift and overshoot animations
 */
internal class IconLiftCanvasDrawable(
        @FloatRange(from = 0.0, to = 1.0)
        private val liftStartProportion: Float,
        private val bounceAnimationData: AnimationData,
        private val colorChangeAnimationData: AnimationData,
        @FloatRange(from = 1.0)
        private val maxIconScaleProportion: Float,
        private val iconInsetPx: Int,
        @Px
        private val iconSizePx: Int,
        private val iconColor: ColorChange,
        private val icon: Drawable
) : CanvasDrawable {

    override var invalidateCallback: () -> Unit = {}

    private var bounceAnimation: CanvasAnimation? = null
    private var colorAnimation: CanvasAnimation? = null

    private var currentLiftProportion: Float = 1.0F

    @ColorInt
    private var currentIconColor: Int = iconColor.start

    override fun onDraw(canvas: Canvas, parentMetrics: ParentMetrics, canvasY: Float, proportion: Float) {
        // Adjust lift
        when {
            proportion >= liftStartProportion && proportion < bounceAnimationData.startProportion && bounceAnimation == null -> {
                val liftRange = bounceAnimationData.startProportion - liftStartProportion
                val currentRangeProportion = (liftRange - (bounceAnimationData.startProportion - proportion)) / liftRange

                currentLiftProportion = 1.0F + ((maxIconScaleProportion - 1.0F) * currentRangeProportion)
            }
            proportion >= bounceAnimationData.startProportion && bounceAnimation == null -> {
                val bounceAnimator = createIconBounceAnimator()
                bounceAnimation = CanvasAnimation(
                        animator = bounceAnimator
                )

                bounceAnimator.start()
            }
            bounceAnimation?.hasEnded == false -> {
            } // Skip here such that we don't reset the lift proportion
            else -> {
                currentLiftProportion = 1.0F
            }
        }

        // Adjust color
        when {
            proportion < colorChangeAnimationData.startProportion && colorAnimation == null -> {
                currentIconColor = iconColor.start
            }
            proportion >= colorChangeAnimationData.startProportion && colorAnimation == null -> {
                val colorAnimator = createColorInterpolationAnimator()
                colorAnimation = CanvasAnimation(
                        animator = colorAnimator
                )

                colorAnimator.start()
            }
            colorAnimation?.hasEnded == true -> {
                currentIconColor = iconColor.end
            }
        }

        drawIcon(canvas, parentMetrics, canvasY)
    }

    override fun reset() {
        colorAnimation?.animator?.cancel()
        bounceAnimation?.animator?.cancel()
        bounceAnimation = null
        colorAnimation = null
        currentLiftProportion = 1.0F
        currentIconColor = iconColor.start
    }

    private fun drawIcon(canvas: Canvas, parentMetrics: ParentMetrics, canvasY: Float) {
        canvas.drawDrawable(
                canvasY = canvasY,
                centerCoordinate = Coordinate(
                        x = parentMetrics.width - iconInsetPx.toFloat(),
                        y = parentMetrics.height / 2F
                ),
                drawable = icon,
                colorInt = currentIconColor,
                iconSizePx = (iconSizePx * currentLiftProportion).toInt())
    }

    private fun createIconBounceAnimator() = ValueAnimator().apply {
        setFloatValues(currentLiftProportion, 1F)
        interpolator = OvershootInterpolator(3.0F)
        duration = bounceAnimationData.duration
        addUpdateListener { animator ->
            currentLiftProportion = animator.animatedValue as Float
            invalidateCallback()
        }
        doOnEnd {
            bounceAnimation = bounceAnimation?.copy(hasEnded = true)
        }
    }

    private fun createColorInterpolationAnimator() = ValueAnimator().apply {
        val (start, end) = iconColor
        setIntValues(start, end)
        setEvaluator(ArgbEvaluator())
        duration = colorChangeAnimationData.duration
        interpolator = LinearInterpolator()
        addUpdateListener { animator ->
            currentIconColor = animator.animatedValue as Int
            invalidateCallback()
        }
        doOnEnd {
            colorAnimation = colorAnimation?.copy(hasEnded = true)
        }
    }
}