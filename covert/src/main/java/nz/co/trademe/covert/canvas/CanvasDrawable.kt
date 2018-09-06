package nz.co.trademe.covert.canvas

import android.graphics.Canvas
import nz.co.trademe.covert.model.ParentMetrics

/**
 * Base interface for all things animatable on a canvas.
 */
internal interface CanvasDrawable {

    /**
     * Callback used to trigger the next frame to be drawn
     */
    var invalidateCallback: () -> Unit

    /**
     * Function invoked to draw an item on the canvas
     */
    fun onDraw(canvas: Canvas, parentMetrics: ParentMetrics, canvasY: Float, proportion: Float)

    /**
     * Function invoked to tell the drawable to clear all state.
     */
    fun reset()
}