package com.example.simpcontrol

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ControlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isrunning)

        val titleView = findViewById<TextView>(R.id.isrunningTitle)
        val debugView = findViewById<TextView>(R.id.debug)
        val touchAreaView = findViewById<View>(R.id.touch_area)

        val mode = intent.getStringExtra("mode")
        var titleText:String = ""

        when(mode){
            "trackpadControl" -> {
                titleText=getString(R.string.mode_trackpad)
                TrackpadHandler.run(touchAreaView, debugView, this)
            }
            "advanceTrackpadControl" -> {
                titleText=getString(R.string.mode_advance_trackpad)
                AdvanceTrackpadHandler.run(touchAreaView, debugView, this)
            }
            "gyroControl" -> {
                titleText=getString(R.string.mode_gyro)
                GyroHandler.run(touchAreaView, debugView, this)
            }
            "advanceGyroControl" -> {
                titleText=getString(R.string.mode_advance_gyro)
                AdvanceGyroHandler.run(touchAreaView, debugView, this)
            }
        }

        titleView.text = titleText

        println(titleText)

    }
}