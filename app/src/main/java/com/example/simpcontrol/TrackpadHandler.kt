package com.example.simpcontrol

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.Error
import kotlin.math.abs
import kotlin.properties.Delegates

class TrackpadHandler {
    val networkHandler = NetworkHandler()

    private lateinit var serverIP:String
    private var serverPort by Delegates.notNull<Int>()
    private var sensitivity by Delegates.notNull<Int>()

    private var mVelocityTracker: VelocityTracker? = null

    private var isLeftClick: Boolean = false
    private var isRightClick: Boolean = false
    private var clickCounter: Int = 0

    private var sendPackets = false


    private var isMove: Boolean = false

    private var timeMillisecond: Long = 0
    private var newTimeMillisecond: Long = 0

    fun run(touchArea: View, debug: TextView, context: Context) {
        debug.text = context.getString(R.string.ready)

        val sharedPref = context.getSharedPreferences(UserSettings.PREFERENCE,
            AppCompatActivity.MODE_PRIVATE
        )
        serverIP = sharedPref.getString(UserSettings.SERVERIP,"192.168.0.1").toString()
        serverPort = sharedPref.getInt(UserSettings.SERVERPORT,8008).toString().toInt()
        sensitivity = sharedPref.getInt(UserSettings.SENSITIVITY,50).toString().toInt()

        try {
            println("[#]  testing reach to: $serverIP")
            networkHandler.testConnection(serverIP){
                if(it){
                    println("[#]  successfully reach: $serverIP")
                    networkHandler.connect(serverIP, serverPort){
                        sendPackets=true
                    }
                }else{
                    println("[#]  fail to reach: $serverIP")
                }
            }
        }catch (err: Error){
            println(err)
        }


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
                        isLeftClick = timeDiff < 300
//                            println("diff: ${timeDiff}, newTime: ${newTimeMillisecond}, prevTime: ${timeMillisecond}")
                        timeMillisecond = newTimeMillisecond

                    }

                    MotionEvent.ACTION_MOVE -> {
                        mVelocityTracker?.apply {
                            val pointerId: Int = event.getPointerId(event.actionIndex)
                            val sens = 0.5
                            addMovement(event)
                            computeCurrentVelocity(sensitivity)

                            val xVel = getXVelocity(pointerId)
                            val yVel = getYVelocity(pointerId)

                            if ( (abs(xVel) < sens) && (abs(yVel) < sens) && !isMove) {
                                "touched on x:${pointX}, y:${pointY}".also { debug.text = it }
                            } else {
                                isMove = true
                                "moved on x:${pointX}, y:${pointY}\nwith velocity: X:${xVel}, Y:${yVel}".also {
                                    debug.text = it
                                }
                            }

                            if (isLeftClick) {
                                debug.text = "${debug.text}\nleftclicked"
                            }

                            println(sendPackets)
//                            if(sendPackets && (abs(xVel) > 0.002f) && (abs(yVel) > 0.002f)){
                            if(sendPackets){
                                val packedData = networkHandler.packData("trackpadControl", xVel, yVel, isLeftClick, false)
//                                println(packedData.toList())
                                networkHandler.sendUDP(packedData)
//                                println(packedData.toList())
                            }
                        }
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        //debug.text = ""
                        mVelocityTracker?.recycle()
                        mVelocityTracker = null
                        isMove = false
                        if (isLeftClick) {
                            val packedData = networkHandler.packData("trackpadControl", 0.0f, 0.0f, false, false)
                            networkHandler.sendUDP(packedData)
                            isLeftClick = false
                        }
//                        //for testing only
//                        if (isLeftClick) {
//                            debug.text = "leftclicked"
//                        }
                    }

                }
                return true
            }
        })
    }
}