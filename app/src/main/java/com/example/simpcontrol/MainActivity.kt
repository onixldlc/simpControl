package com.example.simpcontrol

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet.Motion

class MainActivity : AppCompatActivity() {
    private var mVelocityTracker: VelocityTracker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val trackpadButton = findViewById<TextView>(R.id.trackpad_mode_selection)
        val advanceTrackpadButton = findViewById<TextView>(R.id.advance_trackpad_mode_selection)
        val gyroButton = findViewById<TextView>(R.id.gyro_mode_selection)
        val advanceGyroButton = findViewById<TextView>(R.id.advance_gyro_mode_selection)

        var mode = ""

        trackpadButton.setOnClickListener{
            mode = "trackpadControl"
            runControl(mode)
        }
        advanceTrackpadButton.setOnClickListener{
            mode = "advanceTrackpadControl"
            runControl(mode)
        }
        gyroButton.setOnClickListener {
            mode = "gyroControl"
            runControl(mode)
        }
        advanceGyroButton.setOnClickListener {
            mode = "advanceGyroControl"
            runControl(mode)
        }

    }

    private fun runControl(mode: String){
        startActivity(Intent(this, ControlActivity::class.java).apply {
            // you can add values(if any) to pass to the next class or avoid using `.apply`
            putExtra("mode", mode)
        })
    }

    private fun trackpadControl(){
        setContentView(R.layout.activity_isrunning)
        val title = findViewById<TextView>(R.id.isrunningTitle)
        val debug = findViewById<TextView>(R.id.debug)
        val touchArea = findViewById<View>(R.id.touch_area)

//        println(title)
//        println(title?.text)

        title.text = getString(R.string.mode_trackpad)
        debug.text = getString(R.string.not_implemented_yet)


//        touchArea.setOnGenericMotionListener(object : View.OnGenericMotionListener {
//            @SuppressLint("ClickableViewAccessibility")
//            override fun onGenericMotion(v: View?, event: MotionEvent?): Boolean {
//                println(event)
//                "x:${event?.x.toString()}, y:${event?.y.toString()}".also { debug.text = it }
//
////                when (event?.action) {
////                    MotionEvent.ACTION_DOWN -> //Do Something
////                }
//
//                return v?.onGenericMotionEvent(event) ?: true
//            }
//        })

        touchArea.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility", "Recycle")
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val pointX = event?.x
                val pointY = event?.y


                when (event?.actionMasked) {
//                    MotionEvent.TOOL_TYPE_FINGER -> "moved on x:${pointX}, y:${pointY}".also { debug.text = it }
                    MotionEvent.ACTION_DOWN -> {
                        mVelocityTracker?.clear()
                        mVelocityTracker = mVelocityTracker ?: VelocityTracker.obtain()
                        mVelocityTracker?.addMovement(event)
//                        "tapped on x:${pointX}, y:${pointY}".also { debug.text = it }
//                        println(mVelocityTracker)
//                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        mVelocityTracker?.apply {
                            val pointerId: Int = event.getPointerId(event.actionIndex)
                            val sens = 0.03
                            addMovement(event)
                            computeCurrentVelocity(50)

                            val xVel = getXVelocity(pointerId)*-1
                            val yVel = getYVelocity(pointerId)*-1

                            if((-sens < xVel && xVel < sens) && (-sens < yVel && yVel < sens)){
                                "touched on x:${pointX}, y:${pointY}".also { debug.text = it }
                            }else{
                                "moved on x:${pointX}, y:${pointY}\nwith velocity: X:${xVel}, Y:${yVel}".also { debug.text = it }
                            }

                        }
                        println(mVelocityTracker)

                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        // Return a VelocityTracker object back to be re-used by others.
                        mVelocityTracker?.recycle()
                        mVelocityTracker = null
                    }
                    MotionEvent.AXIS_GESTURE_SCROLL_X_DISTANCE -> {
                        println(event)
                    }

                    MotionEvent.EDGE_RIGHT -> setContentView(R.layout.activity_main)
                    MotionEvent.EDGE_LEFT -> setContentView(R.layout.activity_main)
                }

//                return v?.onTouchEvent(event) ?: true
                return true
            }
        })

//        touchArea.setOnDragListener(object : View.OnDragListener {
//            @SuppressLint("ClickableViewAccessibility")
//            override fun onDrag(v: View?, event: DragEvent?): Boolean {
//                println(event)
//                "x:${event?.x.toString()}, y:${event?.y.toString()}".also { debug.text = it }
////                "not implemented".also { debug.text = it }
//
//                return v?.onDragEvent(event) ?: true
//            }
//        })

    }


    private fun gyroControl(){
        setContentView(R.layout.activity_isrunning)
        val title = findViewById<TextView>(R.id.isrunningTitle)
        val debug = findViewById<TextView>(R.id.debug)
        val touchArea = findViewById<View>(R.id.touch_area)


        title.text = getString(R.string.mode_gyro)
        debug.text = getString(R.string.not_implemented_yet)

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)



        touchArea.setOnDragListener(object : View.OnDragListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onDrag(v: View?, event: DragEvent?): Boolean {
                println(event)
                "not implemented".also { debug.text = it }

                return v?.onDragEvent(event) ?: true
            }
        })
    }
}