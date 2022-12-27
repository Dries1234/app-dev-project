package com.example.aoopproject.classes

import RecyclerAdapter
import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.aoopproject.R
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.lang.Exception

class ProductFragment : Fragment() {

    private lateinit var button: Button
    private lateinit var contentResolver: ContentResolver
    private lateinit var recyclerView: RecyclerView
    companion object {
        fun newInstance(item: JSONObject): ProductFragment {
            val fragment = ProductFragment()
            val args = Bundle()
            args.putString("item", item.toString())
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for the fragment
        contentResolver = requireContext().contentResolver
        val view = inflater.inflate(R.layout.product_fragment, container, false)

        // Initialize any views or widgets that the fragment will use
        val productName = view.findViewById<TextView>(R.id.productName)
        val nova = view.findViewById<TextView>(R.id.nova)
        val productImage = view.findViewById<ImageView>(R.id.productImage)
        val nutriImageView = view.findViewById<ImageView>(R.id.nutriscore)
        button = view.findViewById<Button>(R.id.favourite)

        // Retrieve the item from the arguments
        val itemString = arguments?.getString("item")
        val item = itemString?.let { JSONObject(it) }

        val name: String? = try {

            if (item?.getString("product_name_en") != "")
                item?.getString("product_name_en")
            else item.getString("product_name")
        } catch (e: Exception){
            try {
                item?.getString("product_name")
            }catch(e: Exception){
                "No product name found"
            }
        }
        val brand: String? = try {
            item?.getJSONArray("brands_tags")?.get(0) as String
        }catch (e: Exception){
            "No brands found"
        }
        productName.text = getString(R.string.product_name, brand ,name)

        val novascore: String? = try {
            item?.getString("nova_group")
        } catch(e: Exception){
            "No Nova score found"
        }
        nova.text = getString(R.string.nova_score, novascore)

        Picasso.get().load(item?.getString("image_url")).into(productImage)
        // nutriscore image
        val imgprovider = ImageProvider()
        val nutriscore: String? = try {
            item?.getString("nutriscore_grade")
        }catch (e: Exception) {
            "unknown"
        }
        val nutriImage = imgprovider.getNutriScore(nutriscore);
        nutriImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), nutriImage))
        val nutriments = view.findViewById<TextView>(R.id.nutriments)
        val nutriTitle = view.findViewById<TextView>(R.id.nutrimentsTitle)
        val ingredients = view.findViewById<TextView>(R.id.ingredients)
        nutriTitle.text = getString(R.string.nutriments, item?.getString("nutrition_data_per"))
        nutriTitle.setTypeface(null, Typeface.BOLD)
        val nutrimentsData = item?.getJSONObject("nutriments")
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

            if (item?.getString("ingredients_text_en") != "")
                item?.getString("ingredients_text_en")
            else item.getString("ingredients_text")
        } catch (e: Exception){
            try {
                item?.getString("ingredients_text")
            }catch(e: Exception){
                "No ingredients found"
            }
        }
        ingredients.text = ingredientsText
        // Return the root view for the fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = requireActivity().findViewById(R.id.recycler_view)

        val itemString = arguments?.getString("item")
        val productJSON = itemString?.let { JSONObject(it) }
        button.setOnClickListener {
            val code = productJSON?.getString("_id")
            val cursor = contentResolver.query(Uri.withAppendedPath(Contract.BASE_CONTENT_URI, "favourite/$code"), null,code,null,null)
            try {
                cursor?.moveToFirst()
                val bcode = cursor?.getString(cursor?.getColumnIndexOrThrow(DbHelper.COL_BARCODE))
                contentResolver.delete(Uri.withAppendedPath(Contract.BASE_CONTENT_URI, "favourite/$bcode"),null,null)
            }
            catch (e: Exception){
                val values = ContentValues()
                values.put(DbHelper.COL_BARCODE , code)
                values.put(DbHelper.COL_NAME,productJSON?.getString("product_name") )
                contentResolver.insert(Uri.withAppendedPath(Contract.BASE_CONTENT_URI, "favourite"), values)
            }
            cursor?.close()
            (recyclerView.adapter as RecyclerAdapter).update()
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }
}