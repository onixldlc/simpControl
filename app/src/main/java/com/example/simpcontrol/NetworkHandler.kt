package com.example.simpcontrol

import java.net.DatagramSocket
import java.net.InetAddress

class NetworkHandler private constructor(ip: String, port: Int) {
    private var socket: DatagramSocket
    private val ip: String = ""
    private val port: Int = 0
    private val packer = Packer()

    init {
        val ipAdr = InetAddress.getByName(ip)
        socket = DatagramSocket(port,ipAdr)
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
        TODO("")
    }
}