package com.example.aoopproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


class MainActivity : AppCompatActivity() {
    lateinit var camera: Button;
    lateinit var textView: TextView;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        camera = findViewById<Button>(R.id.camerabutton)

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
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            val productview =  Intent(this, ProductViewer::class.java)
            productview.putExtra("code", result.contents)
            startActivity(productview)
        }
    }
}