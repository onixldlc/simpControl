package com.example.simpcontrol

import android.content.Context
import android.view.VelocityTracker
import android.view.View
import android.widget.TextView

class AdvanceGyroHandler {
    companion object {
        private var mVelocityTracker: VelocityTracker? = null
        fun run(touchArea: View, debug: TextView, context: Context){
            debug.text = context.getString(R.string.not_implemented_yet)
        }
    }
}