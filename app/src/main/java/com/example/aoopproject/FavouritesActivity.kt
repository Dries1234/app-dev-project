package com.example.aoopproject

import RecyclerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aoopproject.classes.APIHandler
import com.example.aoopproject.classes.DbHelper
import com.google.android.material.navigation.NavigationView
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class FavouritesActivity : AppCompatActivity(){
    lateinit var recyclerAdapter : RecyclerAdapter
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)
        recyclerView = findViewById(R.id.recycler_view)
        getFavourites()

    }


    private fun getFavourites() {
        val db = DbHelper(this, null)
        val cursor = db.getFavourites();
        val dataSet = arrayListOf<JSONObject>()
        val lock = MutableLiveData<Boolean>()
        while (cursor.moveToNext()) {
            lock.postValue(false)
            val api =
                APIHandler(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_BARCODE)));
            api.data.observe(this) {

                val json = it.getJSONObject("product");
                dataSet.add(json)
                println("Data: " + dataSet)
                lock.postValue(true)
            }

        }
        lock.observe(this) {
            if(it) {
                config(dataSet)
            }
        }
    }

    private fun config(array:ArrayList<JSONObject>){
        println(array)
        recyclerAdapter = RecyclerAdapter(array, this)
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}