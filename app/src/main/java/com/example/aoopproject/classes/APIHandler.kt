package com.example.aoopproject.classes


import android.content.Context

import androidx.lifecycle.MutableLiveData
import com.android.volley.Request

import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import org.json.JSONObject


class APIHandler (code: String, context: Context){
    var data : MutableLiveData<JSONObject> = MutableLiveData()
    private val url = "https://world.openfoodfacts.org/api/v0/product/${code}.json"
    private val con = context
    init {
        useResult()
    }
    private fun useResult(){
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                if(response.has("code")) {
                    data.postValue(response)
                }
            },
            { error ->
                println("Error requesting product: $error")
            }
        )
        var queue = Volley.newRequestQueue(con)
        queue.add(jsonObjectRequest)
    }
}