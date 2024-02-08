package com.example.simpcontrol

import android.app.Application

class UserSettings: Application() {
    public val preference = "preference"


    public fun getServerIP():String{
        return serverIP
    }
    public fun setServerIP(newServerIP: String){
        serverIP = newServerIP
    }

    public fun getServerPort():Int{
        return serverPort
    }
    public fun setServerPort(newServerPort: Int){
        serverPort = newServerPort
    }

    companion object {
        const val PREFERENCE = "preference"
        const val SERVERIP = "serverip"
        const val SENSITIVITY = "sensitivity"
        const val SERVERPORT = "serverport"


        var serverIP:String = "192.168.0.1"
        var serverPort:Int = 8008
        var sensitivity:Int = 50
    }

}