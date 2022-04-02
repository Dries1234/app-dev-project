package com.example.aoopproject


import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.aoopproject.classes.APIHandler
import com.example.aoopproject.classes.ImageProvider
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.lang.Exception


class ProductViewer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_viewer)

        val code = intent.getStringExtra("code") as String
        val productJSON = APIHandler(code, this)
        productJSON.data.observe(this, Observer {
            setContent(productJSON.data.value)
        })
    }

    private fun setContent(response : JSONObject?){
        // view declaration
        val productName = findViewById<TextView>(R.id.productName)
        val nova = findViewById<TextView>(R.id.nova)
        val productImage = findViewById<ImageView>(R.id.productImage)
        val nutriImageView = findViewById<ImageView>(R.id.nutriscore)

        // product name
        val product = response?.getJSONObject("product")
        productName.text = getString(R.string.product_name,
            product?.getJSONArray("brands_tags")?.get(0), product?.getString("product_name"))

        // nova score
        val novascore: String? = try {
            product?.getString("nova_group")
        } catch(e: Exception){
            "No Nova score found"
        }
        nova.text = getString(R.string.nova_score, novascore)

        // product image
        Picasso.get().load(product?.getString("image_url")).into(productImage)

        // nutriscore image
        val imgprovider = ImageProvider();
        val nutriscore: String? = try {
            product?.getString("nutriscore_grade")
        }catch (e: Exception) {
            "Not found"
        }
        if(nutriscore != "Not found") {
            val nutriImage = imgprovider.getNutriScore(nutriscore);
            nutriImageView.setImageDrawable(ContextCompat.getDrawable(this, nutriImage))
        }

    }
}