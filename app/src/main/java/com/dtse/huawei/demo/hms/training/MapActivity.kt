package com.dtse.huawei.demo.hms.training

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.MapsInitializer;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.CameraPosition;
import com.huawei.hms.maps.model.Circle;
import com.huawei.hms.maps.model.CircleOptions;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;

class MapActivity : AppCompatActivity(), OnMapReadyCallback{

    companion object {
        private const val TAG = "MapViewDemoActivity"
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        private const val REQUEST_CODE = 100
        private val LAT_LNG = LatLng(48.893478, 2.334595)
        private val RUNTIME_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
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

        setContentView(R.layout.activity_map)


        if (!MapActivity.hasPermissions(this, *MapActivity.RUNTIME_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this,
                MapActivity.RUNTIME_PERMISSIONS,
                MapActivity.REQUEST_CODE
            )
        }

        // get mapView by layout view
        mMapView = findViewById(R.id.mapView)
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MapActivity.MAPVIEW_BUNDLE_KEY)
        }
        // please replace "Your API key" with api_key field value in
        // agconnect-services.json if the field is null.
        MapsInitializer.setApiKey("DQEDAKTYcq1mhewV9q7OCESguXABizUPPUROM6fDHYJAMs3Ys3qlvHoEd73tKJYzidevm0RgWKCJ1STiew356xg/UkWyYiNH/1wVIw==")
        MapsInitializer.initialize(applicationContext)
        mMapView.onCreate(mapViewBundle)

        // get map by async method
        mMapView.getMapAsync(this)


    }

    private fun setLocationEnabled(enable: Boolean) {
        hMap?.isMyLocationEnabled = enable
        hMap?.uiSettings?.isMyLocationButtonEnabled = enable
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MapActivity.REQUEST_CODE) {
            setLocationEnabled(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MapActivity.MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MapActivity.MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mMapView.onSaveInstanceState(mapViewBundle)
    }


    override fun onMapReady(map: HuaweiMap) {
        Log.e(TAG, "onMapReady: ")
        hMap = map
        if (MapActivity.hasPermissions(
                this,
                MapActivity.RUNTIME_PERMISSIONS[0],
                MapActivity.RUNTIME_PERMISSIONS[1]
            )
        ) {
            setLocationEnabled(true)
        }

        // move camera by CameraPosition param ,latlag and zoom params can set here
        val build = CameraPosition.Builder().target(MapActivity.LAT_LNG).zoom(10f).build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(build)
        hMap?.animateCamera(cameraUpdate)

        // mark can be add by HuaweiMap


        // circle can be add by HuaweiMap
        mCircle = hMap?.addCircle(CircleOptions().center(LatLng(48.793478, 2.334595)).radius(1000.0).fillColor(
            Color.GREEN))
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




}