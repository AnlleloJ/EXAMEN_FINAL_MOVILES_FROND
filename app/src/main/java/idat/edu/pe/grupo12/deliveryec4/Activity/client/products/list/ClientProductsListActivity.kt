package idat.edu.pe.grupo12.deliveryec4.Activity.client.products.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import idat.edu.pe.grupo12.deliveryec4.Proveedores.ProductsProvider
import idat.edu.pe.grupo12.deliveryec4.R
import idat.edu.pe.grupo12.deliveryec4.adapters.ProductsAdapter
import idat.edu.pe.grupo12.deliveryec4.model.Product
import idat.edu.pe.grupo12.deliveryec4.model.Usuario
import idat.edu.pe.grupo12.deliveryec4.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientProductsListActivity : AppCompatActivity() {

    var TAG= "ClientProducts"

    var recyclerViewProducts: RecyclerView? = null
    var adapter: ProductsAdapter? = null
    var user: Usuario? = null
    var sharedPref: SharedPref? =null
    var productsProvider: ProductsProvider?= null
    var products: ArrayList<Product> = ArrayList()

    var idCategory: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_list)
        sharedPref= SharedPref(this)

        idCategory = intent.getStringExtra("id_categoria")

        getUserFromSession()
        productsProvider = ProductsProvider(user?.sessionToken!!)

        recyclerViewProducts = findViewById(R.id.recycleview_products)
        recyclerViewProducts?.layoutManager = GridLayoutManager(this, 1) // El número depende de la cantidad a mostrar en

      getProducts()                                                                                  //  la sección producto. (puede cambiarse)
    }

    private fun getUserFromSession(){
        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), Usuario::class.java)

        }
    }

    private fun getProducts(){
        productsProvider?.findByCategory(idCategory!!)?.enqueue(object: Callback<ArrayList<Product>>{
            override fun onResponse(call: Call<ArrayList<Product>>, response: Response<ArrayList<Product>>) {
                if (response.body() != null) {
                    products = response.body()!!
                    adapter = ProductsAdapter(this@ClientProductsListActivity, products)
                    recyclerViewProducts?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                Toast.makeText(this@ClientProductsListActivity, t.message, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Error: ${t.message}")
            }

        })
    }



}