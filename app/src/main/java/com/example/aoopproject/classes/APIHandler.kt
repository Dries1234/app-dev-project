package com.example.aoopproject.classes


import android.content.Context

import androidx.lifecycle.MutableLiveData

import kotlinx.coroutines.*

import org.json.JSONObject
import java.net.URL


class APIHandler (code: String){
    var data : MutableLiveData<JSONObject> = MutableLiveData()
    private val endpoint = "https://world.openfoodfacts.org/api/v0/product/${code}.json"
    private val scope = CoroutineScope(Dispatchers.Default)
    init {
        scope.launch {
            getProduct()
        }
    }
    private fun getProduct(){
            val jsonString = URL(endpoint).readText()
            val jsonData = JSONObject(jsonString)
            data.postValue(jsonData)

    }
}