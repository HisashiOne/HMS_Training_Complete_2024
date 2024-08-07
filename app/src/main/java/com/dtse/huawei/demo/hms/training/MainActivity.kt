package com.dtse.huawei.demo.hms.training
//DQEDAKTYcq1mhewV9q7OCESguXABizUPPUROM6fDHYJAMs3Ys3qlvHoEd73tKJYzidevm0RgWKCJ1STiew356xg/UkWyYiNH/1wVIw==


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.common.ApiException
import com.huawei.hms.push.HmsMessaging

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.ActivityCompat


import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.MapsInitializer;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.CameraPosition;
import com.huawei.hms.maps.model.Circle;
import com.huawei.hms.maps.model.CircleOptions;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val TAG = "MapViewDemoActivity"
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        private const val REQUEST_CODE = 100
        private val LAT_LNG = LatLng(48.893478, 2.334595)
        private val RUNTIME_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE)

        /**
         * checkSelfPermission
         *
         * @param context     Context
         * @param permissions permissions
         * @return true or false
         */
        private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false
                    }
                }
            }
            return true
        }
    }

    private lateinit var mMapView: MapView
    private var hMap: HuaweiMap? = null
    private var mMarker: Marker? = null
    private var mCircle: Circle? = null

    val TAG = "HMS_Core"
    private var token = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        MapsInitializer.setApiKey("DQEDAKTYcq1mhewV9q7OCESguXABizUPPUROM6fDHYJAMs3Ys3qlvHoEd73tKJYzidevm0RgWKCJ1STiew356xg/UkWyYiNH/1wVIw==")
        MapsInitializer.initialize(applicationContext)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        if (!hasPermissions(this, *RUNTIME_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE)
        }

        // get mapView by layout view
        mMapView = findViewById(R.id.mapView)
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        // please replace "Your API key" with api_key field value in
        // agconnect-services.json if the field is null.
        MapsInitializer.setApiKey("DQEDAKTYcq1mhewV9q7OCESguXABizUPPUROM6fDHYJAMs3Ys3qlvHoEd73tKJYzidevm0RgWKCJ1STiew356xg/UkWyYiNH/1wVIw==")
        MapsInitializer.initialize(applicationContext)
        mMapView.onCreate(mapViewBundle)

        // get map by async method
        mMapView.getMapAsync(this)

        getToken()

    }

    private fun setLocationEnabled(enable: Boolean) {
        hMap?.isMyLocationEnabled = enable
        hMap?.uiSettings?.isMyLocationButtonEnabled = enable
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            setLocationEnabled(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mMapView.onSaveInstanceState(mapViewBundle)
    }


    override fun onMapReady(map: HuaweiMap) {
        Log.e(TAG, "onMapReady: ")
        hMap = map
        if (hasPermissions(this, RUNTIME_PERMISSIONS[0], RUNTIME_PERMISSIONS[1])) {
            setLocationEnabled(true)
        }

        // move camera by CameraPosition param ,latlag and zoom params can set here
        val build = CameraPosition.Builder().target(LAT_LNG).zoom(10f).build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(build)
        hMap?.animateCamera(cameraUpdate)

        // mark can be add by HuaweiMap


        // circle can be add by HuaweiMap
        mCircle = hMap?.addCircle(CircleOptions().center(LatLng(48.793478, 2.334595)).radius(1000.0).fillColor(Color.GREEN))
        mCircle?.fillColor = Color.TRANSPARENT
    }

    override fun onStart() {
        super.onStart()
        mMapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
    }

    override fun onPause() {
        mMapView?.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
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