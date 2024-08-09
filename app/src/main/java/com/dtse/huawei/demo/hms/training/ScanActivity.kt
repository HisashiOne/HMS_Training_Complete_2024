package com.dtse.huawei.demo.hms.training

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions

class ScanActivity : AppCompatActivity() {

    val TAG = "HMS_Core " + ScanActivity::class.java.getName()

    companion object {
        // CODE FOR DEFAULT VIEW
        private const val DEFINED_CODE = 222
        private const val REQUEST_CODE_SCAN = 0X01

        // CODE FOR CUSTOM VIEW
        private const val DEFINED_CODE_CUSTOM = 333
        private const val REQUEST_CODE_SCAN_CUSTOM = 0X02

        // CODE FOR BITMAP VIEW
        const val BITMAP = 0x22
        const val REQUEST_CODE_PHOTO = 0x33

    }

    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)

        button1.setOnClickListener {
            newViewBtnClick()
        }
        button2.setOnClickListener {
            newViewBtnClickCustom()
        }
        button3.setOnClickListener {
            newViewBtnClickBitmap(it)
        }

    }

    private fun newViewBtnClick() {
        // Initialize a list of required permissions to request runtime
        val list = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, list, DEFINED_CODE)
    }

    private fun newViewBtnClickCustom() {
        // Initialize a list of required permissions to request runtime
        val list = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, list, DEFINED_CODE_CUSTOM)
    }

    private fun newViewBtnClickBitmap(view: View?) {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            BITMAP
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions == null || grantResults == null || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            return
        } else if (requestCode == DEFINED_CODE) {
            // Call the barcode scanning view in Default View mode.
            ScanUtil.startScan(
                this,
                REQUEST_CODE_SCAN,
                HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create()
            )
        } else if (requestCode == DEFINED_CODE_CUSTOM) {
            //start your activity for scanning barcode
            this.startActivityForResult(
                Intent(this, CustomScanActivity::class.java), REQUEST_CODE_SCAN_CUSTOM
            )
        } else if (requestCode == BITMAP) {
            // Call the system album.
            val pickIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            this@ScanActivity.startActivityForResult(pickIntent, REQUEST_CODE_PHOTO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //receive result after your activity finished scanning
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }
        // Obtain the return value of HmsScan from the value returned by the onActivityResult method by using ScanUtil.RESULT as the key value.
        else if (requestCode == REQUEST_CODE_SCAN) {
            when (val obj: Parcelable? = data.getParcelableExtra(ScanUtil.RESULT)) {
                is HmsScan -> {
                    if (!TextUtils.isEmpty(obj.getOriginalValue())) {
                        Toast.makeText(this, obj.getOriginalValue(), Toast.LENGTH_SHORT).show()
                    }
                    return
                }
            }
        } else if (requestCode == REQUEST_CODE_SCAN_CUSTOM) {
            // Obtain the return value of HmsScan from the value returned by the onActivityResult method by using ScanUtil.RESULT as the key value.
            val hmsScan: HmsScan? = data.getParcelableExtra(CustomScanActivity.SCAN_RESULT)
            if (hmsScan != null) {
                if (!TextUtils.isEmpty(hmsScan.getOriginalValue()))
                    Toast.makeText(this, hmsScan.getOriginalValue(), Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == REQUEST_CODE_PHOTO) {
            // Obtain the image path.
            val path = getImagePath(this@ScanActivity, data)
            if (TextUtils.isEmpty(path)) {
                return
            }
            // Obtain the bitmap from the image path.
            val bitmap = ScanUtil.compressBitmap(this@ScanActivity, path)
            // Call the decodeWithBitmap method to pass the bitmap.
            val result1 = ScanUtil.decodeWithBitmap(
                this@ScanActivity,
                bitmap,
                HmsScanAnalyzerOptions.Creator().setHmsScanTypes(0).setPhotoMode(false).create()
            )
            // Obtain the scanning result.
            if (result1 != null && result1.size > 0) {
                if (!TextUtils.isEmpty(result1[0].getOriginalValue())) {
                    Toast.makeText(this, result1[0].getOriginalValue(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    /** getImagePath form intent
     */
    private fun getImagePath(context: Context, data: Intent): String? {
        var imagePath: String? = null
        val uri = data.data
        //get api version
        val currentapiVersion = Build.VERSION.SDK_INT
        if (currentapiVersion > Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                if ("com.android.providers.media.documents" == uri!!.authority) {
                    val id = docId.split(":").toTypedArray()[1]
                    val selection = MediaStore.Images.Media._ID + "=" + id
                    imagePath = getImagePath(
                        context,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        selection
                    )
                } else if ("com.android.providers.downloads.documents" == uri.authority) {
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        docId.toLong()
                    )
                    imagePath = getImagePath(context, contentUri, null)
                } else {
                    Log.i(TAG, "getImagePath  uri.getAuthority():" + uri.authority)
                }
            } else if ("content".equals(uri!!.scheme, ignoreCase = true)) {
                imagePath = getImagePath(context, uri, null)
            } else {
                Log.i(TAG, "getImagePath  uri.getScheme():" + uri.scheme)
            }
        } else {
            imagePath = getImagePath(context, uri, null)
        }
        return imagePath
    }

    /**
     * get image path from system album by uri
     */
    private fun getImagePath(context: Context, uri: Uri?, selection: String?): String? {
        var path: String? = null
        val cursor = context.contentResolver.query(uri!!, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }
}


