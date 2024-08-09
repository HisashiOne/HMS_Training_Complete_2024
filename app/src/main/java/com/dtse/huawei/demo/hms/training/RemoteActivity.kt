package com.dtse.huawei.demo.hms.training

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.huawei.agconnect.remoteconfig.AGConnectConfig


class RemoteActivity : AppCompatActivity() {

    val GREETING_KEY: String = "GREETING_KEY"
    val SET_BOLD_KEY: String = "SET_BOLD_KEY"
    private var config: AGConnectConfig? = null
    private var textView: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote)

        textView = findViewById(R.id.greeting)
        val button = findViewById<Button>(R.id.fetch_button)
        config = AGConnectConfig.getInstance()

        button.setOnClickListener { fetchAndApply() }
    }


    private fun fetchAndApply() {

       config!!.fetch(0)
            .addOnSuccessListener { configValues -> // Apply Network Config to Current Config
                config!!.apply(configValues)
                updateUI()
            }.addOnFailureListener { e ->
                Log.e("HMS_Core",  "fetch setting failed: " + e )
                textView!!.text = "fetch setting failed: " + e.message

            }
    }

    private fun updateUI() {
        val text = config!!.getValueAsString(GREETING_KEY)
        val isBold = config!!.getValueAsBoolean(SET_BOLD_KEY)
        textView!!.text = text
        if (isBold) {
            textView!!.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }
    }
}