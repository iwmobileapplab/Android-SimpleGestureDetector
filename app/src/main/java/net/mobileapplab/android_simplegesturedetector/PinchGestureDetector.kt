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

import android.view.MotionEvent

class PinchGestureDetector(private val pinchGestureListener: PinchGestureListener) {

    private var mScale = 1.0f

    private var mCurrentSpan: Float = 0f

    private var mPrevSpan: Float = 0f

    val scaleFactor: Float
        get() = if (mPrevSpan > 0) mCurrentSpan / mPrevSpan else 1f

    interface PinchGestureListener {
        fun onScale(pinchGestureDetector: PinchGestureDetector)
    }

    @Synchronized
    fun onTouchEvent(event: MotionEvent): Boolean {
        val count = event.pointerCount
        if (count != 2) {
            return false
        }

        val x1 = event.getX(0) * mScale
        val y1 = event.getY(0) * mScale

        val x2 = event.getX(1) * mScale
        val y2 = event.getY(1) * mScale

        when (event.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                mCurrentSpan = getVector(x1, x2, y1, y2)
                pinchGestureListener.onScale(this)
                mScale = Math.min(1.23E10f, mScale * (mCurrentSpan / mPrevSpan))
                mPrevSpan = mCurrentSpan

                return true
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                mCurrentSpan = getVector(x1, x2, y1, y2)
                mPrevSpan = mCurrentSpan
            }

            MotionEvent.ACTION_POINTER_UP -> mScale = 1f
        }
        return false
    }

    private fun getVector(x1: Float, x2: Float, y1: Float, y2: Float): Float {
        val dx = x1 - x2
        val dy = y1 - y2
        return Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
    }
}