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
import com.google.gson.Gson
import idat.edu.pe.grupo12.deliveryec4.Activity.client.home.ClientHomeActivity
import idat.edu.pe.grupo12.deliveryec4.Activity.client.products.detail.ClientProductsDetailActivity
import idat.edu.pe.grupo12.deliveryec4.Activity.client.products.list.ClientProductsListActivity
import idat.edu.pe.grupo12.deliveryec4.Activity.restaurant.home.RestaurantHomeActivity
import idat.edu.pe.grupo12.deliveryec4.R
import idat.edu.pe.grupo12.deliveryec4.model.Category
import idat.edu.pe.grupo12.deliveryec4.model.Product
import idat.edu.pe.grupo12.deliveryec4.model.Rol
import idat.edu.pe.grupo12.deliveryec4.utils.SharedPref

class ProductsAdapter (val context: Activity, val products: ArrayList<Product>): RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_product, parent, false)
        return ProductsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    //metodo para especificar valores de vista
    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {

        val product = products[position]//cada categoria

        holder.textViewName.text = product.nombre
        holder.textViewPrice.text = "S/.${product.precio}" //aqui asigamos el tipo de moneda
        //Especificar imagen a visualizar
        Glide.with(context).load(product.imagen1).into(holder.imageViewProduct)



        holder.itemView.setOnClickListener { goToDetail(product) }
    }
    private fun goToDetail(product: Product){

        val gson = Gson()

        val i = Intent(context, ClientProductsDetailActivity::class.java)
        i.putExtra("product", product.toJson())
        context.startActivity(i)
    }




    class ProductsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewName : TextView
        val textViewPrice: TextView
        val imageViewProduct: ImageView

        init {
            textViewName = view.findViewById(R.id.textView_productName)
            textViewPrice = view.findViewById(R.id.textView_productPrice)
            imageViewProduct = view.findViewById(R.id.imageview_product)
        }
    }
}