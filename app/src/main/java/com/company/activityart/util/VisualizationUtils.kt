package com.company.activityart.util

import android.content.Context
import android.graphics.*
import android.util.Size
import androidx.annotation.Px
import com.company.activityart.domain.models.Activity
import com.company.activityart.presentation.editArtScreen.StrokeWidthType
import com.google.maps.android.PolyUtil
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

class VisualizationUtils @Inject constructor(private val context: Context) {

    companion object {
        private const val ACTIVITY_SIZE_REDUCE_FRACTION = 0.85f
        private const val ACTIVITY_STROKE_SMALL_REDUCE_FRACTION = 0.15f
        private const val ACTIVITY_STROKE_MEDIUM_REDUCE_FRACTION = 0.25f
        private const val ACTIVITY_STROKE_LARGE_REDUCE_FRACTION = 0.50f
        private const val OFFSET_ZERO_PX = 0f
    }

    fun createBitmap(
        activities: List<Activity>,
        colorActivitiesArgb: Int,
        colorBackgroundArgb: Int,
        colorFontArgb: Int,
        bitmapSize: Size,
        fontAssetPath: String,
        fontSize: FontSizeType,
        strokeWidth: StrokeWidthType,
        @Px paddingFraction: Float = 0.1f,
        textLeft: String? = null,
        textCenter: String? = null,
        textRight: String? = null
    ): Bitmap {
        val textMeasurementLeft = Rect()
        val textMeasurementCenter = Rect()
        val textMeasurementRight = Rect()

        val fontTextSize = minOf(bitmapSize.height, bitmapSize.width) * when (fontSize) {
            FontSizeType.XS -> 0.025f
            FontSizeType.SMALL -> 0.05f
            FontSizeType.MEDIUM -> 0.075f
            FontSizeType.LARGE -> 0.1f
            FontSizeType.XL -> 0.125f
        }

        val textPaint = Paint().apply {
            color = colorFontArgb
            textSize = fontTextSize
            isAntiAlias = true
            typeface = Typeface.createFromAsset(context.assets, fontAssetPath)
            textCenter?.let { getTextBounds(it, 0, it.length, textMeasurementCenter) }
            textLeft?.let { getTextBounds(it, 0, it.length, textMeasurementLeft) }
            textRight?.let { getTextBounds(it, 0, it.length, textMeasurementRight) }
        }

        val maxTextHeight = listOf(
            textMeasurementLeft.height(),
            textMeasurementCenter.height(),
            textMeasurementRight.height()
        ).max()

        return Bitmap.createBitmap(
            bitmapSize.width,
            bitmapSize.height,
            Bitmap.Config.ARGB_8888
        ).also { bitmap ->
            Canvas(bitmap).apply {
                drawBackground(colorBackgroundArgb)

                val padding = paddingFraction * minOf(width, height)
                val paddingOnEachSide = (padding * 2f).toInt()

                val height = bitmapSize.height
                val width = bitmapSize.width
                val centerX = width / 2f

                computeDrawingSpecification(
                    n = activities.size,
                    height = height - paddingOnEachSide - maxTextHeight -
                            (maxTextHeight.takeIf { it > 0 }
                                ?.coerceAtMost((paddingFraction * bitmapSize.height).toInt())
                                ?.toFloat() ?: 0f).toInt(),
                    width = width - paddingOnEachSide
                ).apply {

                    val extraSpaceEachSideWidth = extraSpaceWidth / 2f

                    textLeft?.let {
                        drawText(
                            it,
                            extraSpaceEachSideWidth + padding,
                            height - padding - (maxTextHeight / 2f),
                            textPaint
                        )
                    }

                    textCenter?.let {
                        drawText(
                            it,
                            centerX - (textMeasurementCenter.width() / 2f),
                            height - padding - (maxTextHeight / 2f),
                            textPaint
                        )
                    }

                    textRight?.let {
                        drawText(
                            it,
                            width - extraSpaceEachSideWidth - padding - textMeasurementRight.width(),
                            height - padding - (maxTextHeight / 2f),
                            textPaint
                        )
                    }

                    val finalRowOffset = (activitySize * remainder) / 2f
                    activities.forEachIndexed { index, activity ->
                        // 0 % 1 = 0
                        // 0 * 606 = 0

                        PolyUtil.decode(activity.summaryPolyline).let { latLngList ->

                            /** Zero-indexed row and column **/
                            val col = index % cols
                            val row = (floor(index / cols.toFloat()) % rows).toInt()

                            /** Adjust for zero-index **/
                            val isFinalRow = row == (rows - 1)

                            val xOffset =
                                ((index % cols) * activitySize) + (activitySize / 2f) + (extraSpaceWidth / 2f) + padding + if (isFinalRow) finalRowOffset else 0f
                            val yOffset =
                                ((floor(index / cols.toFloat()) % rows) * activitySize) + (activitySize / 2f) + (extraSpaceHeight / 2f) + padding

                            val left = latLngList.minOf { it.longitude }
                            val right = latLngList.maxOf { it.longitude }
                            val top = latLngList.maxOf { it.latitude }
                            val bottom = latLngList.minOf { it.latitude }

                            val largestSide = maxOf(top - bottom, right - left)
                            val multiplier =
                                (activitySize * ACTIVITY_SIZE_REDUCE_FRACTION) / largestSide

                            latLngList.map { latLng ->
                                Pair(
                                    first = (((latLng.longitude - ((
                                            left + right
                                            ) / 2f)) * multiplier) + xOffset).toFloat(),
                                    second = (((latLng.latitude - ((
                                            top + bottom
                                            ) / 2f)) * -1f * multiplier) + yOffset).toFloat()
                                )
                            }

                            // Reduce List<LatLng> to Path
                        }.let { floatList ->
                            val path = Path().also { path ->
                                floatList.forEachIndexed { fIndex, pair ->
                                    if (fIndex == 0)
                                        path.setLastPoint(pair.first, pair.second)
                                    else
                                        path.lineTo(pair.first, pair.second)
                                }
                            }

                            drawPath(path, Paint().also {
                                it.color = colorActivitiesArgb
                                it.style = Paint.Style.STROKE
                                it.strokeJoin = Paint.Join.ROUND
                                it.strokeWidth = sqrt(activitySize) * when (strokeWidth) {
                                    StrokeWidthType.THIN -> ACTIVITY_STROKE_SMALL_REDUCE_FRACTION
                                    StrokeWidthType.MEDIUM -> ACTIVITY_STROKE_MEDIUM_REDUCE_FRACTION
                                    StrokeWidthType.THICK -> ACTIVITY_STROKE_LARGE_REDUCE_FRACTION
                                }
                                it.isAntiAlias = true
                            })
                        }
                    }
                }
            }
        }
    }

    private fun Canvas.drawBackground(argb: Int) {
        drawRect(
            OFFSET_ZERO_PX,
            OFFSET_ZERO_PX,
            width.toFloat(),
            height.toFloat(),
            Paint().apply { color = argb }
        )
    }

    private data class DrawingSpecification(
        val activitySize: Float,
        val cols: Int,
        val rows: Int,
        val remainder: Int,
        val extraSpaceWidth: Int,
        val extraSpaceHeight: Int
    )

    private fun computeDrawingSpecification(
        n: Int,
        height: Int,
        width: Int
    ): DrawingSpecification {

        val ratio = width / height.toFloat()
        val colsFloat = sqrt(n * ratio)
        val rowsFloat = n / colsFloat

        var rows1 = ceil(rowsFloat)
        var cols1 = ceil(n / rows1)
        while (rows1 * ratio < cols1) {
            rows1++
            cols1 = ceil(n / rows1)
        }
        val cellsize1 = height / rows1

        var cols2 = ceil(colsFloat)
        var rows2 = ceil(n / cols2)
        while (cols2 < rows2 * ratio) {
            cols2++
            rows2 = ceil(n / cols2)
        }
        val cellsize2 = width / cols2

        return if (cellsize1 < cellsize2) {
            DrawingSpecification(
                activitySize = cellsize2,
                cols = cols2.toInt(),
                rows = rows2.toInt(),
                remainder = ((rows2 * cols2) - n).toInt(),
                extraSpaceHeight = (height - (rows2 * cellsize2)).toInt(),
                extraSpaceWidth = 0
            )
        } else {
            DrawingSpecification(
                activitySize = cellsize1,
                cols = cols1.toInt(),
                rows = rows1.toInt(),
                remainder = ((rows1 * cols1) - n).toInt(),
                extraSpaceHeight = 0,
                extraSpaceWidth = (width - (cols1 * cellsize1)).toInt()
            )
        }
    }
}