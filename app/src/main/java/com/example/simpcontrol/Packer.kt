package com.example.simpcontrol

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Optional

class Packer {
//    val finalByteArray:ByteArray = byteArrayOf()

    fun packMultiArray(arrayOfByteArray: Array<ByteArray>):ByteArray{
        var finalByteArray = ByteArray(0)
        for (byteArray in arrayOfByteArray){
            finalByteArray = finalByteArray.plus(byteArray)
        }
        return finalByteArray
    }

    fun packMode(mode:String):ByteArray{
        var modeByte = 0
        when(mode){
            "trackpadControl" -> modeByte = 2
            "advanceTrackpadControl" -> modeByte = 3
            "gyroControl" -> modeByte = 4
            "advanceGyroControl" -> modeByte = 5
        }
        return packChar(modeByte.toChar())
    }
    private fun packDouble(double: Double): ByteArray {
        val converted = double.toLong()
        return packLong(converted)
    }
    fun packFloat(float: Float):ByteArray{
        val converted = float.toInt()
        return packInt(converted)
    }
    fun packBoolean(boolean: Boolean):ByteArray{
        var bool = 0
        if(boolean){
            bool = 1
        }
        return packChar(bool.toChar())
    }

    private fun packLong(long: Long):ByteArray{
        val buffer = ByteBuffer.allocate(Long.SIZE_BYTES)
        buffer.putLong(long)
        return buffer.array()
    }

    private fun packInt(int: Int):ByteArray{
        val buffer = ByteBuffer.allocate(Int.SIZE_BYTES)
        buffer.putInt(int)
        return buffer.array()
    }

    private fun packChar(char: Char):ByteArray{
        val buffer = ByteBuffer.allocate(Char.SIZE_BYTES)
        buffer.putChar(char)
        return buffer.array()
    }
}