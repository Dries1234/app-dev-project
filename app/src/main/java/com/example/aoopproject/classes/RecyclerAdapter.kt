import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.example.aoopproject.ProductViewer
import com.example.aoopproject.R
import com.example.aoopproject.classes.APIHandler
import com.example.aoopproject.classes.DbHelper
import com.squareup.picasso.Picasso
import org.json.JSONObject


class RecyclerAdapter (private var dataSet: ArrayList<JSONObject>, private val context: Context) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(), LifecycleOwner {
    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val imageView: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.favourite_name)
            imageView = view.findViewById(R.id.favourite_image)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.favourites_card, viewGroup, false)

        return ViewHolder(view)
    }



    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.setOnClickListener {
            val productView = Intent(context, ProductViewer::class.java)
            productView.putExtra("code", dataSet[position].getString("_id"))
            context.startActivity(productView)
        }
        viewHolder.textView.text = dataSet[position].getString("product_name")
        Picasso.get().load(dataSet[position].getString("image_url")).into(viewHolder.imageView)

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun update() {
        val db = DbHelper(this.context, null)
        val cursor = db.getFavourites();
        this.dataSet = arrayListOf<JSONObject>()
        while (cursor.moveToNext()) {
            val api =
                APIHandler(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_BARCODE)));
            api.data.observe(this) {
                val json = api.data.value?.getJSONObject("product");
                if (json != null) {
                    this.dataSet.add(json)
                }
            }

        }

    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }


}

