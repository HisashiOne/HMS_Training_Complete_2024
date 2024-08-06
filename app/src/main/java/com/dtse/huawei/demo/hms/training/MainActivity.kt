package com.dtse.huawei.demo.hms.training

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.common.ApiException
import com.huawei.hms.push.HmsMessaging

class MainActivity : AppCompatActivity() {


    val TAG = "HMS_Core"
    private var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val myPushService = MyPushService()

        getToken()

    }


    private fun getToken() {
        // Create a thread.
        object : Thread() {
            override fun run() {
                try {
                    // Obtain the app ID from the agconnect-services.json file.
                    val appId = "111699329"

                    // Set tokenScope to HCM.
                    val tokenScope = "HCM"
                    token = HmsInstanceId.getInstance(this@MainActivity).getToken(appId, tokenScope)
                    Log.e(TAG, "Get Token: $token")
                    /*tokenView?.let { setText(it, "Get Token: $token") }
                    // Check whether the token is null.
                    if (!TextUtils.isEmpty(token)) {
                        sendRegTokenToServer(token)
                    }*/
                } catch (e: ApiException) {
                    Log.e(TAG, "Get Token Failed, $e")
                    //tokenView?.let { setText(it, "Get Token Failed, $e") }
                    ///tokenView!!.text = "get token failed, $e"
                }
            }
        }.start()
    }



    private fun subscribe(topic: String?) {
        try {
            // Subscribe to a topic.
            HmsMessaging.getInstance(this@MainActivity)
                .subscribe(topic)
                .addOnCompleteListener { task ->
                    // Obtain the topic subscription result.
                    if (task.isSuccessful) {
                        Log.i(TAG, "subscribe topic successfully")
                    } else {
                        Log.e(TAG, "subscribe topic failed, the return value is " + task.exception.message)
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "subscribe failed, catch exception : $e")
        }
    }
}