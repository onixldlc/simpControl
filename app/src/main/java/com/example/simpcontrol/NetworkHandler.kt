package com.example.simpcontrol

import android.os.AsyncTask
import java.lang.Error
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.Queue
import java.util.concurrent.Callable
import java.util.concurrent.Executor

class NetworkHandler: NetworkThread() {
    private var retunableBoolean = false

    private val socket = DatagramSocket()
    private val packer = Packer()

    fun testConnection(serverIP: String) {
        testConnection(serverIP) {
            println(it)
        }
    }

    fun testConnection(serverIP: String, callback: (s: Boolean) -> Unit) {
        execute{
            val ipAdr = InetAddress.getByName(serverIP)
            val result = ipAdr.isReachable(5)
            callback(result)
        }
    }


    fun connect(serverIP: String, serverPort: Int) {
        connect(serverIP, serverPort){}
    }

    fun connect(serverIP: String, serverPort: Int, callback: (s: Boolean) -> Unit) {
        execute{
            val result = try {
                val ipAdr = InetAddress.getByName(serverIP)
                socket.connect(ipAdr, serverPort)
                true
            }catch (err: Error){
                println(err)
                false
            }
            callback(result)
        }
    }

    fun close(){
        socket.close()
    }


    fun packData(
        mode: String,
        padVelX: Float,
        padVelY: Float,
        mouse1: Boolean,
        mouse2: Boolean
    ): ByteArray {
        val packedMode = packer.packMode(mode)
        val packedPadVelX = packer.packFloat(padVelX)
        val packedPadVelY = packer.packFloat(padVelY)
        val packedM1 = packer.packBoolean(mouse1)
        val packedM2 = packer.packBoolean(mouse2)

        val packedArray = arrayOf(packedMode, packedPadVelX, packedPadVelY, packedM1, packedM2)
        return packer.packMultiArray(packedArray)

    }

    public fun sendUDP(data:ByteArray){
        execute{
            try {
                socket.send(DatagramPacket(data, data.size))
            }catch (err: Error){
                println(err)
            }
        }
    }

    fun runAsync(callback: () -> Unit) {
        execute{
            callback()
        }
    }
}