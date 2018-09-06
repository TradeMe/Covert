package nz.co.trademe.covert.model

/**
 * Data class describing a Coordinate. Used instead of [android.graphics.PointF] such that named arguments can be used
 */
internal data class Coordinate(
        val x: Float,
        val y: Float
)