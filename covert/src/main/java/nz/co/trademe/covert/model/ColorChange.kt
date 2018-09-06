package nz.co.trademe.covert.model

import android.support.annotation.ColorInt

/**
 * Data class describing a color change
 */
internal data class ColorChange(
        @ColorInt val start: Int,
        @ColorInt val end: Int
)