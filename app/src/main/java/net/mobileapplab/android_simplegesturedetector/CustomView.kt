/*
 * Copyright (C) 2018 Mobile Application Lab
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.mobileapplab.android_simplegesturedetector

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import net.mobileapplab.simplegesture.library.DragGestureDetector
import net.mobileapplab.simplegesture.library.PinchGestureDetector
import net.mobileapplab.simplegesture.library.RotateGestureDetector

class CustomView(context: Context?, attrs: AttributeSet?) : View(context, attrs),
        DragGestureDetector.DragGestureListener, PinchGestureDetector.PinchGestureListener, RotateGestureDetector.RotateGestureListener {

    companion object {
        private const val MIN_SCALE_FACTOR = 0.5f
        private const val MAX_SCALE_FACTOR = 3.0f
    }

    private val dragGestureDetector = DragGestureDetector(dragGestureListener = this)

    private val pinchGestureDetector = PinchGestureDetector(pinchGestureListener = this)

    private val rotateGestureDetector = RotateGestureDetector(rotateGestureListener = this)

    private var scaleFactor = 1.0f
    private var angle = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragGestureDetector.onTouchEvent(event)
        pinchGestureDetector.onTouchEvent(event)
        rotateGestureDetector.onTouchEvent(event)
        return true
    }

    override fun onScroll(distanceX: Float, distanceY: Float) {
        val point = rotate(0f, distanceX, 0f, distanceY, angle)
        x += point.x * scaleFactor
        y += point.y * scaleFactor
    }

    override fun onScale(pinchGestureDetector: PinchGestureDetector) {
        scaleFactor *= pinchGestureDetector.scaleFactor

        // Don't let the object get too small or too large.
        scaleFactor = Math.max(MIN_SCALE_FACTOR, Math.min(scaleFactor, MAX_SCALE_FACTOR))
        scaleX = scaleFactor
        scaleY = scaleFactor
    }

    override fun onRotation(angle: Float) {
        this@CustomView.angle += angle
        rotation += angle
    }

    private fun rotate(centerX: Float, x: Float, centerY: Float, y: Float, angle: Float): PointF {
        val rad = Math.toRadians(angle.toDouble())
        val rX = (centerX + (x - centerX) * Math.cos(rad) - (y - centerY) * Math.sin(rad)).toFloat()
        val rY = (centerY.toDouble() + (x - centerX) * Math.sin(rad) + (y - centerY) * Math.cos(rad)).toFloat()

        return PointF(rX, rY)
    }
}