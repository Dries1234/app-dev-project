package com.example.aoopproject

import RecyclerAdapter
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.ArraySet
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aoopproject.classes.APIHandler
import com.example.aoopproject.classes.Contract
import com.example.aoopproject.classes.DbHelper
import com.example.aoopproject.classes.ProductFragment
import com.example.aoopproject.classes.Util.Companion.isTablet
import com.google.android.material.navigation.NavigationView
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class FavouritesActivity : AppCompatActivity(){
    lateinit var recyclerAdapter : RecyclerAdapter
    lateinit var recyclerView: RecyclerView
    var dataSet: ArrayList<JSONObject> = arrayListOf()
    private lateinit var fragmentContainer: FrameLayout
    var shouldExecute: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)
        recyclerView = findViewById(R.id.recycler_view)
        getFavourites()

        recyclerView = findViewById(R.id.recycler_view)
        fragmentContainer = findViewById(R.id.fragment_container)

        val orientation = resources.configuration.orientation
        if (orientation != Configuration.ORIENTATION_LANDSCAPE) {
            // Device is not in landscape mode, hide the fragment
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if (fragment != null) {
                transaction.remove(fragment)
            }
            transaction.commit()
        }
        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val child = rv.findChildViewUnder(e.x, e.y)
                if (child != null) {
                    val position = rv.getChildAdapterPosition(child)
                    onItemClicked(position)
                    return true
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }

        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.d("PLS WORK", "This is called")
        super.onConfigurationChanged(newConfig)

        val orientation = newConfig.orientation
        if (isTablet(this)) {
            if (orientation != Configuration.ORIENTATION_LANDSCAPE) {
                val fragment =
                    supportFragmentManager.findFragmentById(R.id.fragment_container)
                val transaction = supportFragmentManager.beginTransaction()
                if (fragment != null) {
                    transaction.remove(fragment)
                }
                transaction.commit()
            }
        }
    }


    override fun onResume() {
        if(shouldExecute){
            recyclerAdapter.update()
        }
        else{
            shouldExecute = true
        }
        super.onResume()
    }

    private fun onItemClicked(position: Int) {
        val item = dataSet[position]

        val orientation = resources.configuration.orientation
        if (isTablet(this)) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                val fragment = ProductFragment.newInstance(item)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, fragment)
                transaction.commit()
            } else {
                val intent = Intent(this, ProductViewer::class.java)
                intent.putExtra("code", item.getString("_id"))
                startActivity(intent)
            }
        }
        else {
            val intent = Intent(this, ProductViewer::class.java)
            intent.putExtra("code", item.getString("_id"))
            startActivity(intent)
        }
    }



    private fun getFavourites() {
        val cursor = contentResolver.query(Uri.withAppendedPath(Contract.BASE_CONTENT_URI, "favourite"), null,null,null,null)
            ?: return
        val lock = MutableLiveData<Boolean>()
        while (cursor.moveToNext()) {
            lock.postValue(false)
            val api =
                APIHandler(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_BARCODE)));
            api.data.observe(this) {
                val json = it.getJSONObject("product");
                dataSet.add(json)
                lock.postValue(true)
            }

        }
        cursor.close()
        lock.observe(this) {
            if(it) {
                config(dataSet)
            }
        }
    }

    private fun config(array:ArrayList<JSONObject>){
        recyclerAdapter = RecyclerAdapter(array, this)
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}