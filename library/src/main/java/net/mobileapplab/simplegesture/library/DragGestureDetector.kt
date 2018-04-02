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
import android.view.MotionEvent.INVALID_POINTER_ID

class DragGestureDetector(private val dragGestureListener: DragGestureListener) {

    private var mInitialEvent: MotionEvent? = null

    private var mActivePointerId: Int = 0

    interface DragGestureListener {
        fun onScroll(distanceX: Float, distanceY: Float)
    }

    @Synchronized
    fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mActivePointerId = event.getPointerId(0)
                mInitialEvent = MotionEvent.obtain(event)
            }

            MotionEvent.ACTION_POINTER_DOWN ->
                if (mActivePointerId == INVALID_POINTER_ID) {
                    mActivePointerId = event.getPointerId(event.actionIndex)
                    mInitialEvent = MotionEvent.obtain(event)
                }

            MotionEvent.ACTION_MOVE -> {
                if (mActivePointerId == INVALID_POINTER_ID) {
                    return false
                }

                val pointerIndex = event.findPointerIndex(mActivePointerId)

                val x = event.getX(pointerIndex)
                val y = event.getY(pointerIndex)

                mInitialEvent?.let {
                    val distanceX = x - it.x
                    val distanceY = y - it.y
                    dragGestureListener.onScroll(distanceX, distanceY)
                }
            }

            MotionEvent.ACTION_UP -> {
                mActivePointerId = INVALID_POINTER_ID
                mInitialEvent = null
            }

            MotionEvent.ACTION_CANCEL -> {
                mActivePointerId = INVALID_POINTER_ID
                mInitialEvent = null
            }

            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = event.actionIndex
                val pointerId = event.getPointerId(pointerIndex)

                if (pointerId == mActivePointerId) {
                    mActivePointerId = INVALID_POINTER_ID
                    mInitialEvent = null
                }
            }

            else -> {
                // NOP.
            }
        }
        return true
    }
}