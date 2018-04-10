# Android-SimpleGestureDetector
Detects simple gestures using supplied MotionEvents.

![image](https://github.com/iwmobileapplab/Android-SimpleGestureDetector/blob/master/sample.gif)
## Usage
[Copy detector classes](https://github.com/iwmobileapplab/Android-SimpleGestureDetector/tree/master/library/src/main/java/net/mobileapplab/simplegesture/library) into your projects.

## Integration
### [An example for the custom view](https://github.com/iwmobileapplab/Android-SimpleGestureDetector/blob/master/app/src/main/java/net/mobileapplab/android_simplegesturedetector/CustomView.kt#L28):
```kotlin
class CustomView(context: Context?, attrs: AttributeSet?) : View(context, attrs),
        DragGestureDetector.DragGestureListener, PinchGestureDetector.PinchGestureListener, RotateGestureDetector.RotateGestureListener {
```

Initialize detectors.
```kotlin
    private val dragGestureDetector = DragGestureDetector(dragGestureListener = this)

    private val pinchGestureDetector = PinchGestureDetector(pinchGestureListener = this)

    private val rotateGestureDetector = RotateGestureDetector(rotateGestureListener = this)
```

Override View#onTouchEvent method and give motion event to detectors for analyzes.
```kotlin
    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragGestureDetector.onTouchEvent(event)
        pinchGestureDetector.onTouchEvent(event)
        rotateGestureDetector.onTouchEvent(event)
        return true
    }
```

## Doc
### [DragGestureDetector](https://github.com/iwmobileapplab/Android-SimpleGestureDetector/blob/master/library/src/main/java/net/mobileapplab/simplegesture/library/DragGestureDetector.kt)
Detects simple drag gesture.
#### [DragGestureListener#onScroll(distanceX: Float, distanceY: Float)](https://github.com/iwmobileapplab/Android-SimpleGestureDetector/blob/master/library/src/main/java/net/mobileapplab/simplegesture/library/DragGestureDetector.kt#L29)
Notified when a drag gesture detected.

CustomView uses DragGestureListener to tracking the finger location.
```kotlin
    override fun onScroll(distanceX: Float, distanceY: Float) {
        val point = rotate(0f, distanceX, 0f, distanceY, angle)
        x += point.x * scaleFactor
        y += point.y * scaleFactor
    }
```

---
### [PinchGestureDetector](https://github.com/iwmobileapplab/Android-SimpleGestureDetector/blob/master/library/src/main/java/net/mobileapplab/simplegesture/library/PinchGestureDetector.kt)
Detects simple pinch gesture between two fingers.
#### [PinchGestureListener#onScale(pinchGestureDetector: PinchGestureDetector)](https://github.com/iwmobileapplab/Android-SimpleGestureDetector/blob/master/library/src/main/java/net/mobileapplab/simplegesture/library/PinchGestureDetector.kt#L33)
Notified when a pinch gesture detected.

CustomView uses PinchGestureListener to scale up/down according to the scaleFactor.
```kotlin
    override fun onScale(pinchGestureDetector: PinchGestureDetector) {
        scaleFactor *= pinchGestureDetector.scaleFactor

        // Don't let the object get too small or too large.
        scaleFactor = Math.max(MIN_SCALE_FACTOR, Math.min(scaleFactor, MAX_SCALE_FACTOR))
        scaleX = scaleFactor
        scaleY = scaleFactor
    }
```

---
### [RotateGestureDetector](https://github.com/iwmobileapplab/Android-SimpleGestureDetector/blob/master/library/src/main/java/net/mobileapplab/simplegesture/library/RotateGestureDetector.kt)
Detects simple rotate gesture between two fingers.
#### [RotateGestureListener#onRotation(angle: Float)](https://github.com/iwmobileapplab/Android-SimpleGestureDetector/blob/master/library/src/main/java/net/mobileapplab/simplegesture/library/RotateGestureDetector.kt#L24)
Notified when a rotate gesture detected.

CustomView uses PinchGestureListener to rotate the given angle from the two fingers movement.
```kotlin
    override fun onRotation(angle: Float) {
        this@CustomView.angle += angle
        rotation += angle
    }
```
