package com.dtse.huawei.demo.hms.training

import android.os.Bundle
import android.util.Log
import com.huawei.hms.push.HmsMessageService
import java.lang.Exception

class MyPushService : HmsMessageService() {
    val TAG = "HMS_Core"
    override fun onNewToken(token: String?, bundle: Bundle?) {
        Log.e(TAG, "have received refresh token:$token")
    }

    override fun onTokenError(p0: Exception?) {
        super.onTokenError(p0)
        Log.e(TAG, "On Token Error: $p0")
    }
}