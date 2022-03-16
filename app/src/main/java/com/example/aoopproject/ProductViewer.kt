package com.example.aoopproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ProductViewer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_viewer)
        val productName = findViewById<TextView>(R.id.productName)
        val code = intent.getStringExtra("code")
        val url = "https://world.openfoodfacts.org/api/v0/product/${code}.json"
        Log.d("URL" , url)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                Log.d("FULL JSON", response.toString(2))
                val product = response.getJSONObject("product")
                productName.text = product.getString("product_name")

            },
            { error ->
                productName.text = "WTF"
            }
        )
        val queue = Volley.newRequestQueue(this)
        queue.add(jsonObjectRequest)

    }
}