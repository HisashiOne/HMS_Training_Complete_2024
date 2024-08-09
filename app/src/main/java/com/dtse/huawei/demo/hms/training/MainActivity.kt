package com.dtse.huawei.demo.hms.training

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.common.ApiException
import com.huawei.hms.push.HmsMessaging

import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.ListView


class MainActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // use arrayadapter and define an array
        val arrayAdapter: ArrayAdapter<*>
        val users = arrayOf(
            "HMS Push Kit", "HMS Map Kit", "HMS Analytics Kit",
            "HMS IAP Kit", "HMS Account Kit",
            "HMS ML Scan Kit", "HMS Remote Config"
        )

        // access the listView from xml file
        var mListView = findViewById<ListView>(R.id.userlist)
        arrayAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, users)
        mListView.adapter = arrayAdapter

        mListView.setOnItemClickListener{ parent, view, position, id ->

            var intent: Intent? = null
            when(position) {
                0 -> intent = Intent(this, PushActivity::class.java)
                1 -> intent = Intent(this, MapActivity::class.java)
                2 -> intent = Intent(this, AnalyticsActivity::class.java)
                3 -> intent = Intent(this, IAPActivity::class.java)
                4 -> intent = Intent(this, AccountActivity::class.java)
                5 -> intent = Intent(this, ScanActivity::class.java)
                6 -> intent = Intent(this, RemoteActivity::class.java)

                else -> {

                }
            }

            startActivity(intent)

        }

        //getToken()

    }



}