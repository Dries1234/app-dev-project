package com.example.aoopproject.classes

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import java.util.*

class APIHandler (code: String, context: Context, custom: (JSONObject) -> Unit){
    var data : JSONObject? = null
    private val url = "https://world.openfoodfacts.org/api/v0/product/${code}.json"
    private val con = context
    init {
        Log.d("Thread about to start", "IM STARTING")
        useResult(custom)
    }
    private fun useResult(useResult: (JSONObject) -> Unit){
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("YO work please", "Value assigned")
                data = response
                Log.d("I HAVE VALUE", data.toString())
            },
            { error ->
            }
        )
        var queue = Volley.newRequestQueue(con)
        queue.add(jsonObjectRequest)
    }
}