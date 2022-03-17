package com.example.aoopproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.TextView

import com.example.aoopproject.classes.APIHandler
import org.json.JSONObject


class ProductViewer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_viewer)

        val code = intent.getStringExtra("code") as String
        val context = this
        val productJSON: APIHandler = APIHandler(code,context, ::setContent)
        /*while(productJSON.data == null){
            Log.d("NO INIT", productJSON.data.toString())
        }
        Log.d("YES INIT", "initialized")
        */

    }

    fun setContent(response : JSONObject){
        val productName = findViewById<TextView>(R.id.productName)
        val product = response.getJSONObject("product")
        productName.text = product.getString("product_name")
    }
}