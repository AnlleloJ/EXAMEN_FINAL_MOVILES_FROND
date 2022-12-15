package idat.edu.pe.grupo12.deliveryec4.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import idat.edu.pe.grupo12.deliveryec4.Activity.client.home.ClientHomeActivity
import idat.edu.pe.grupo12.deliveryec4.Activity.client.products.list.ClientProductsListActivity
import idat.edu.pe.grupo12.deliveryec4.Activity.restaurant.home.RestaurantHomeActivity
import idat.edu.pe.grupo12.deliveryec4.R
import idat.edu.pe.grupo12.deliveryec4.model.Category
import idat.edu.pe.grupo12.deliveryec4.model.Rol
import idat.edu.pe.grupo12.deliveryec4.utils.SharedPref

class CategoriesAdapter (val context: Activity, val categories: ArrayList<Category>): RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_categories, parent, false)
        return CategoriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    //metodo para especificar valores de vista
    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {

        val category = categories[position]//cada categoria

        holder.textViewCategory.text = category.nombre
        Glide.with(context).load(category.imagen).into(holder.imageViewCategory)

        holder.itemView.setOnClickListener { goToProducts(category) }
    }
    //reforzar
    private fun goToProducts(category: Category){
        val i = Intent(context, ClientProductsListActivity::class.java)
        i.putExtra("id_categoria", category.id)
        context.startActivity(i)
    }


    class CategoriesViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewCategory : TextView
        val imageViewCategory: ImageView

        init {
            textViewCategory = view.findViewById(R.id.textview_category)
            imageViewCategory = view.findViewById(R.id.imageview_category)
        }
    }
}