package com.antmar.pixflickrappviews.presentation.views

import android.content.Context
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView

class ZoomableImageView @JvmOverloads constructor(
    context: Context,
    attrs : AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    private val matrix = Matrix()
    private val savedMatrix = Matrix()

    private enum class Mode { NONE, DRAG, ZOOM }
    private var mode = Mode.NONE

    private var startX = 0f
    private var startY = 0f
    private var scaleFactor = 1f

    private val minScale = 1f
    private val maxScale = 5f
    private var isZoomed = false

    private val scaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scale = detector.scaleFactor
            scaleFactor *= scale
            scaleFactor = scaleFactor.coerceIn(minScale, maxScale)
            matrix.postScale(scale, scale, detector.focusX, detector.focusY)
            imageMatrix = matrix
            return true
        }
    })

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            if (isZoomed) {
                // Сбросить зум
                matrix.reset()
                imageMatrix = matrix
                scaleFactor = minScale
                isZoomed = false
            } else {
                // Увеличить (2x) в точке касания
                scaleFactor = 2f
                matrix.postScale(scaleFactor, scaleFactor, e.x, e.y)
                imageMatrix = matrix
                isZoomed = true
            }
            return true
        }
    })

    init {
        scaleType = ScaleType.MATRIX
        setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            scaleGestureDetector.onTouchEvent(event)

            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    savedMatrix.set(matrix)
                    startX = event.x
                    startY = event.y
                    mode = Mode.DRAG
                }

                MotionEvent.ACTION_POINTER_DOWN -> {
                    savedMatrix.set(matrix)
                    mode = Mode.ZOOM
                }

                MotionEvent.ACTION_MOVE -> {
                    if (mode == Mode.DRAG) {
                        val dx = event.x - startX
                        val dy = event.y - startY
                        matrix.set(savedMatrix)
                        matrix.postTranslate(dx, dy)
                        imageMatrix = matrix
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                    mode = Mode.NONE
                    performClick()
                }
            }

            true
        }
    }
}