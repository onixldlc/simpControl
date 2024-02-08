package com.example.simpcontrol

import kotlinx.coroutines.delay
import java.util.Queue
import java.util.concurrent.Executor
import kotlin.concurrent.thread


open class NetworkThread: Executor {
    private val tasks: ArrayDeque<Runnable> = ArrayDeque<Runnable>(1)
    private fun startRunning(){
        val thread = Thread {
            println("[#]  task size before loop: ${tasks.size}")
            while(tasks.size > 0){
                println("[#]  task size after loop before exec: ${tasks.size}")
                val runnable = tasks.first()
                println("[#]  runnable: $runnable")
                runnable.run()
                println("[#]  task size after exec: ${tasks.size}")
                tasks.removeFirst()
                println("[#]  task size after removeFirst: ${tasks.size}")
            }
        }
        thread.start()

//        val execFunc = Runnable{
//            while(!tasks.isEmpty()){
//                val runnable = tasks.first()
//                runnable.run()
//                println(tasks.size)
//                tasks.removeFirst()
//            }
//        }
//        val threadWithRunnable = Thread(execFunc())
//        val threading=Thread()
//        threading.run {
//            Runnable{
//                while(!tasks.isEmpty()){
//                    val runnable = tasks.first()
//                    runnable.run()
//                    println(tasks.size)
//                    tasks.removeFirst()
//                }
//            }
//        }
    }

    override fun execute(command: Runnable) {
        println(tasks.size)
        if(tasks.isEmpty()){
            tasks.add(command)
            startRunning()
        }else{
            tasks.add(command)
        }
    }
}