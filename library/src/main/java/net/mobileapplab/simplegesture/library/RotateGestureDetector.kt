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

package net.mobileapplab.simplegesture.library

import android.view.MotionEvent

class RotateGestureDetector(private val rotateGestureListener: RotateGestureListener) {

    interface RotateGestureListener {
        fun onRotation(angle: Float)
    }

    private var x1: Float = 0f
    private var y1: Float = 0f
    private var x2: Float = 0f
    private var y2: Float = 0f

    private var mAngle = 0f

    @Synchronized
    fun onTouchEvent(event: MotionEvent): Boolean {
        val count = event.pointerCount
        if (count != 2) {
            return false
        }

        when (event.actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                x1 = event.getX(0)
                y1 = event.getY(0)
                x2 = event.getX(1)
                y2 = event.getY(1)
            }

            MotionEvent.ACTION_MOVE -> {
                val dx1 = event.getX(0)
                val dy1 = event.getY(0)
                val dx2 = event.getX(1)
                val dy2 = event.getY(1)
                mAngle = angleBetweenLines(x1, y1, x2, y2, dx1, dy1, dx2, dy2)

                rotateGestureListener.onRotation(mAngle)
                return true
            }
        }

        return false
    }

    private fun angleBetweenLines(x1: Float, y1: Float, x2: Float, y2: Float, dx1: Float, dy1: Float, dx2: Float, dy2: Float): Float {
        val angle1 = Math.atan2((y1 - y2).toDouble(), (x1 - x2).toDouble())
        val angle2 = Math.atan2((dy1 - dy2).toDouble(), (dx1 - dx2).toDouble())

        var angle = Math.toDegrees((angle2 - angle1)).toFloat() % 360
        if (angle < -180f) angle += 360.0f
        if (angle > 180f) angle -= 360.0f
        return angle
    }
}