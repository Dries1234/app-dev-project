package com.example.aoopproject

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


val DEBUG: Boolean = true
val CODE: String = "3161711001971"

class MainActivity : AppCompatActivity() {
    lateinit var camera: Button;
    lateinit var textView: TextView;
    lateinit var drawerLayout: DrawerLayout;
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle;

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer);
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        camera = findViewById<Button>(R.id.camerabutton)

        if (!DEBUG) {

            camera.setOnClickListener {
                val options = ScanOptions();
                options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES);
                options.setPrompt("Scan a barcode")
                options.setCameraId(0)
                options.setBeepEnabled(false)
                options.setBarcodeImageEnabled(true)
                options.setOrientationLocked(false)
                barcodeLauncher.launch(options)
            }
        } else {
            camera.setOnClickListener {
                debugProduct()
            }
        }


    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            val productView = Intent(this, ProductViewer::class.java)
            productView.putExtra("code", result.contents)
            startActivity(productView)
        }
    }

    fun debugProduct(){
        val productView = Intent(this, ProductViewer::class.java)
        productView.putExtra("code", CODE)
        startActivity(productView)
    }

}