package nz.co.trademe.covert.model

import android.animation.ValueAnimator

/**
 * Data class for holding information about animations occuring on the canvas
 */
internal data class CanvasAnimation(
        val animator: ValueAnimator,
        val hasEnded: Boolean = false
)