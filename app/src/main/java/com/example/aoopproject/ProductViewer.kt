package com.example.aoopproject


import RecyclerAdapter
import android.content.ContentValues
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.aoopproject.classes.APIHandler
import com.example.aoopproject.classes.Contract
import com.example.aoopproject.classes.DbHelper
import com.example.aoopproject.classes.ImageProvider
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.lang.Exception


class ProductViewer : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_viewer)


        val code = intent.getStringExtra("code") as String
        val productJSON = APIHandler(code)
        val favouriteButton = findViewById<Button>(R.id.favourite)

        productJSON.data.observe(this, Observer {
            favouriteButton.setOnClickListener {
                val cursor = contentResolver.query(Uri.withAppendedPath(Contract.BASE_CONTENT_URI, "favourite/$code"), null,code,null,null)
                try {
                    cursor?.moveToFirst()
                    val bcode = cursor?.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_BARCODE))
                    contentResolver.delete(Uri.withAppendedPath(Contract.BASE_CONTENT_URI, "favourite/$bcode"),null,null)
                }
                catch (e: Exception){
                    val values = ContentValues()
                    values.put(DbHelper.COL_BARCODE , code)
                    values.put(DbHelper.COL_NAME,productJSON.data.value?.getJSONObject("product")!!.getString("product_name") )
                    contentResolver.insert(Uri.withAppendedPath(Contract.BASE_CONTENT_URI, "favourite"), values)
                }
                cursor?.close()
            }

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
        val brand: String? = try {
            product?.getJSONArray("brands_tags")?.get(0) as String
        }catch (e: Exception){
            "No brands found"
        }
        val name: String? = try {

            if (product?.getString("product_name_en") != "")
                product?.getString("product_name_en")
            else product.getString("product_name")
        } catch (e: Exception){
            try {
                product?.getString("product_name")
            }catch(e: Exception){
                "No product name found"
            }
        }
        productName.text = getString(R.string.product_name, brand ,name)


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
        val imgprovider = ImageProvider()
        val nutriscore: String? = try {
            product?.getString("nutriscore_grade")
        }catch (e: Exception) {
            "unknown"
        }
        val nutriImage = imgprovider.getNutriScore(nutriscore)
        nutriImageView.setImageDrawable(ContextCompat.getDrawable(this, nutriImage))

        //nutriments
        val nutriments = findViewById<TextView>(R.id.nutriments)
        val nutriTitle = findViewById<TextView>(R.id.nutrimentsTitle)
        val ingredients = findViewById<TextView>(R.id.ingredients)
        nutriTitle.text = getString(R.string.nutriments, product?.getString("nutrition_data_per"))
        nutriTitle.setTypeface(null, Typeface.BOLD)
        val nutrimentsData = product?.getJSONObject("nutriments")
        try {
            nutriments.text = getString(R.string.concat, nutriments.text , getString(R.string.grams,nutrimentsData?.getString("fat_100g"), "Fat\n"))



        }catch (e:Exception){
            //dont add to string
        }
        try {
            nutriments.text = getString(R.string.concat, nutriments.text,getString(R.string.grams,nutrimentsData?.getString("saturated-fat_100g"), "Saturated fat\n"))
        }catch(e:Exception){
            //dont add to string
        }
        try {
            nutriments.text = getString(R.string.concat, nutriments.text,getString(R.string.grams,nutrimentsData?.getString("sugars_100g"), "Sugars\n"))
        }catch(e:Exception){
            //dont add to string
        }
        try {
            nutriments.text = getString(R.string.concat, nutriments.text,getString(R.string.grams,nutrimentsData?.getString("salt_100g"), "Salt\n"))
        }catch(e:Exception){
            //dont add to string
        }
        val ingredientsText: String? = try {

            if (product?.getString("ingredients_text_en") != "")
                product?.getString("ingredients_text_en")
            else product.getString("ingredients_text")
        } catch (e: Exception){
            try {
                product?.getString("ingredients_text")
            }catch(e: Exception){
                "No ingredients found"
            }
        }
        ingredients.text = ingredientsText
    }
}