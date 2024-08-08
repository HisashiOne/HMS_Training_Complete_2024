package com.dtse.huawei.demo.hms.training

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.common.ApiException
import com.huawei.hms.push.HmsMessaging

class PushActivity : AppCompatActivity() {

    val TAG = "HMS_Core"
    private var token = ""
    private var tokenView: TextView? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_push)
        tokenView = findViewById<TextView>(R.id.tokenTxt)
    }
}