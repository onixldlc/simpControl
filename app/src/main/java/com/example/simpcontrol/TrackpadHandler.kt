package com.example.simpcontrol

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.TextView

class TrackpadHandler {

    companion object {
        private var mVelocityTracker: VelocityTracker? = null

        private var isLeftClick: Boolean = false
        private var isRightClick: Boolean = false
        private var clickCounter: Int = 0

        private var isMove: Boolean = false

        private var timeMillisecond:Long = 0
        private var newTimeMillisecond:Long = 0

        fun run(touchArea: View, debug: TextView, context: Context){
            debug.text = context.getString(R.string.ready)
            touchArea.setOnTouchListener(object : View.OnTouchListener {
                @SuppressLint("ClickableViewAccessibility", "Recycle")
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    val pointX = event?.x
                    val pointY = event?.y

                    when (event?.actionMasked) {
                        MotionEvent.ACTION_DOWN -> {
                            mVelocityTracker?.clear()
                            mVelocityTracker = mVelocityTracker ?: VelocityTracker.obtain()
                            mVelocityTracker?.addMovement(event)

                            newTimeMillisecond = System.currentTimeMillis()
                            val timeDiff = newTimeMillisecond - timeMillisecond
                            isLeftClick = timeDiff < 200
//                            println("diff: ${timeDiff}, newTime: ${newTimeMillisecond}, prevTime: ${timeMillisecond}")
                            timeMillisecond = newTimeMillisecond

                        }
                        MotionEvent.ACTION_MOVE -> {
                            mVelocityTracker?.apply {
                                val pointerId: Int = event.getPointerId(event.actionIndex)
                                val sens = 0.03
                                addMovement(event)
                                computeCurrentVelocity(50)

                                val xVel = getXVelocity(pointerId)*-1
                                val yVel = getYVelocity(pointerId)*-1

                                if((-sens < xVel && xVel < sens) && (-sens < yVel && yVel < sens) && !isMove){

                                    "touched on x:${pointX}, y:${pointY}".also { debug.text = it }
                                    if(isLeftClick){
                                        debug.text = "${debug.text}\nleftclicked"
                                    }

                                }else{
                                    isMove = true
                                    "moved on x:${pointX}, y:${pointY}\nwith velocity: X:${xVel}, Y:${yVel}".also { debug.text = it }
                                    if(isLeftClick){
                                        debug.text = "${debug.text}\nleftclicked"
                                    }
                                }
                            }
                        }
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            //debug.text = ""
                            mVelocityTracker?.recycle()
                            mVelocityTracker = null
                            isMove = false
                            if(isLeftClick) {
                                isLeftClick = false
                            }

                            //for testing only
                            if (isLeftClick){
                                debug.text = "leftclicked"
                            }
                        }
                    }
                    return true
                }
            })
        }
    }
}