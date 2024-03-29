package com.example.simpcontrol

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import java.lang.Error
import kotlin.math.pow

class SettingsActivity : AppCompatActivity() {

//    var backButton: TextView
//    var ipInput: TextView? = null
//    var portInput: TextView? = null
//    var applyButton: TextView? = null
//    var sendTestButton: TextView? = null
//    var pref: SharedPreferences? = null

    var serverIP:String = ""
    var serverPort:Int = 0
    var sensitivity:Int = 0

    var sharedPref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPref = getSharedPreferences(UserSettings.PREFERENCE, MODE_PRIVATE)
        editor = sharedPref?.edit()

        try {

        }catch (err: Error){
            println(err)
        }


//        pref = getSharedPreferences()

        val backButton = findViewById<TextView>(R.id.backButton)

        val ipInput = findViewById<EditText>(R.id.ServerIP)
        val portInput = findViewById<EditText>(R.id.ServerPort)
        val sensInput = findViewById<EditText>(R.id.Sensitifity)

        val applyButton = findViewById<Button>(R.id.apply_setting)
        val sendTestButton = findViewById<Button>(R.id.send_test)
        val statusText = findViewById<TextView>(R.id.config_status)

        loadSharedPreference()

        ipInput.setText(serverIP)
        portInput.setText(serverPort.toString())
        sensInput.setText(sensitivity.toString())

        backButton?.setOnClickListener {
            finish ()
        }

        applyButton?.setOnClickListener{
            saveSetting(statusText)
        }
        sendTestButton?.setOnClickListener{
            testSetting(statusText)
        }

        typingHandler(ipInput) {
            println(it)
            serverIP = it.toString()
        }

        typingHandler(portInput) {
            println(it)
            if(it.isNullOrEmpty()){
                serverPort = -1
            }
            else{
                serverPort = it.toString().toInt()
            }
        }

        typingHandler(sensInput){
            println(it)
            if(it.isNullOrEmpty()){
                sensitivity = -1
            }
            else{
                sensitivity = it.toString().toInt()
            }
        }
    }

    private fun typingHandler(textInput:EditText?, callback: (s: CharSequence?) -> Unit) {
        textInput?.addTextChangedListener(object:TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                callback(s)
            }
        })
    }

    private fun isInputValid(regex: Regex, inputText:CharSequence): Boolean {
        return regex.matches(inputText)
    }

    private fun loadSharedPreference() {
        serverIP = sharedPref?.getString(UserSettings.SERVERIP,"192.168.0.1").toString()
        serverPort = sharedPref?.getInt(UserSettings.SERVERPORT,8008).toString().toInt()
        sensitivity = sharedPref?.getInt(UserSettings.SENSITIVITY,50).toString().toInt()
    }

    private  fun testSetting(statusView:TextView){
        val networkHandler = NetworkHandler()

        networkHandler.testConnection(serverIP){
            if(it){
                statusView.post{
                    statusView.setTextColor(Color.parseColor("#FF58F167"))
                    statusView.text = getString(R.string.success_host_is_reachable)
                }
                networkHandler.connect(serverIP, serverPort)
                val testPacket = arrayOf<Byte>(1, 0,0,0,2, 0,0,0,3, 4, 5).toByteArray()
                networkHandler.sendUDP(testPacket)
            }else{
                statusView.post{
                    statusView.setTextColor(Color.parseColor("#FFF15858"))
                    statusView.text = getString(R.string.error_host_unreachable)
                }
            }
        }
    }

    private fun saveSetting(statusView:TextView){

        statusView.setTextColor(Color.parseColor("#FFF15858"))

        val isServerIpValid = isInputValid("""^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])${'$'}""".toRegex(),serverIP)
        if(!isServerIpValid){
            statusView.text = getString(R.string.error_invalid_ip)
            return
        }

        val isServerPortValid = serverPort > 0 && serverPort < 2.0.pow(16.0).toInt()
        if(!isServerPortValid){
            statusView.text = getString(R.string.error_invalid_port)
            return
        }

        val isSensValid = sensitivity > 0 && sensitivity < 1000
        if(!isSensValid){
            statusView.text = getString(R.string.error_invalid_sensitivity)
            return
        }

        editor?.putString(UserSettings.SERVERIP, serverIP)
        editor?.putInt(UserSettings.SERVERPORT, serverPort)
        editor?.putInt(UserSettings.SENSITIVITY, sensitivity)

        editor?.apply()
        statusView.setTextColor(Color.parseColor("#FF58F167"))
        statusView.text = getString(R.string.success_changes_were_saved)

//        serverIP = ipInput?.text.toString()
//        serverPort = portInput?.text.toString().toInt()
    }
}