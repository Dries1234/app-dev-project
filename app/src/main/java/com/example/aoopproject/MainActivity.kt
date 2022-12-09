package com.example.aoopproject

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.aoopproject.classes.Util
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import java.util.*

//set to true for debug mode
const val DEBUG: Boolean = true
const val CODE: String = "5410013800002"

class MainActivity : AppCompatActivity() {
    lateinit var camera: Button;
    lateinit var textView: TextView;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sf = PreferenceManager.getDefaultSharedPreferences(this)
        Util.applyPreferencedTheme(sf, this)

        val lang = sf.getString(this.getString(R.string.lang_pref), "en")
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        this.createConfigurationContext(config)

        setSupportActionBar(findViewById(R.id.toolbar))

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

    private fun debugProduct(){
        val productView = Intent(this, ProductViewer::class.java)
        productView.putExtra("code", CODE)
        startActivity(productView)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.favourites -> {
                val intent = Intent(this, FavouritesActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}