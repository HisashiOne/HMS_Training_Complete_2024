package com.dtse.huawei.demo.hms.training

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.iap.Iap
import com.huawei.hms.iap.IapApiException
import com.huawei.hms.iap.IapClient
import com.huawei.hms.iap.entity.OrderStatusCode
import com.huawei.hms.iap.entity.ProductInfo
import com.huawei.hms.iap.entity.ProductInfoReq
import com.huawei.hms.iap.entity.PurchaseIntentReq

class IAPActivity : AppCompatActivity() {

    val TAG = "HMS_Core"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iapactivity)

        val button = findViewById<Button>(R.id.products_button)

        button.setOnClickListener{loadProduct()}
        //button.setOnClickListener{gotoPay(this, "hms_training_2024_123", 0)}


    }

    private fun loadProduct() {
        // Obtain in-app product details configured in AppGallery Connect, and then show the products.
        val iapClient = Iap.getIapClient(this)
        val task =iapClient.obtainProductInfo(createProductInfoReq())
        task.addOnSuccessListener { result ->
            Log.e("HMS_Core", "Success Products" + result)
            if (result != null) {
                //showProduct(result.productInfoList)
                Log.e("HMS_Core", result.productInfoList.size.toString())
                Log.e("HMS_Core", "Success Products")
            }
        }.addOnFailureListener { e ->
            Log.e("HMS_Core", "Error Products ")
            e.message?.let { Log.e("IAP", it) }
            if (e is IapApiException) {
                val returnCode = e.statusCode
                if (returnCode == OrderStatusCode.ORDER_HWID_NOT_LOGIN) {
                    Toast.makeText(this, "Please sign in to the app with a HUAWEI ID.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showProduct(productInfoList: List<ProductInfo>) {
        Log.e("HMS_Core", productInfoList.size.toString())
        for (productInfo in productInfoList) {
            var productsinfo = ProductsListModel(
                productInfo.productName,
                productInfo.price,
                productInfo.productId,
            )
            Log.e("HMS_Core", productsinfo.id)

        }
    }


    private fun createProductInfoReq(): ProductInfoReq? {
        // In-app product type contains:
        // 0: consumable.
        // 1: non-consumable.
        // 2: auto-renewable subscription.
        val req = ProductInfoReq()
        req?.let { productDetails ->
            productDetails.priceType = IapClient.PriceType.IN_APP_CONSUMABLE
            val productIds = ArrayList<String>()
            // Pass in the item_productId list of products to be queried.
            // The product ID is the same as that you set during product information configuration in AppGallery Connect.
            productIds.add("Consumable_1")
            productDetails.productIds = productIds
        }
        return req
    }

    private fun gotoPay(activity: Activity, productId: String?, type: Int) {

        Log.i(TAG, "call createPurchaseIntent")
        val mClient = Iap.getIapClient(activity)
        val task = mClient.createPurchaseIntent(createPurchaseIntentReq(type, productId))
        task.addOnSuccessListener(OnSuccessListener { result ->
            Log.i(TAG, "createPurchaseIntent, onSuccess")
            if (result == null) {
                Log.e(TAG, "result is null")
                return@OnSuccessListener
            }
            val status = result.status
            if (status == null) {
                Log.e(TAG, "status is null")
                return@OnSuccessListener
            }
            // you should pull up the page to complete the payment process.
            if (status.hasResolution()) {
                try {
                    //status.startResolutionForResult(activity, REQ_CODE_BUY)
                } catch (exp: IntentSender.SendIntentException) {
                    Log.e(TAG, exp.message.toString())
                }
            } else {
                Log.e(TAG, "intent is null")
            }
        }).addOnFailureListener { e ->
            Log.e(TAG, e.message.toString())
            Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            if (e is IapApiException) {
                val returnCode = e.statusCode
                Log.e(TAG, "createPurchaseIntent, returnCode: $returnCode")
                // handle error scenarios
            }
        }
    }


    private fun createPurchaseIntentReq(type: Int, productId: String?): PurchaseIntentReq? {
        val req = PurchaseIntentReq()
        req?.let { productDetails ->
            productDetails.productId = productId
            productDetails.priceType = type
            productDetails.developerPayload = "test"
        }
        return req
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }



}