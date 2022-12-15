package idat.edu.pe.grupo12.deliveryec4.fragments.client

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.gson.Gson
import idat.edu.pe.grupo12.deliveryec4.Proveedores.CategoriesProvider
import idat.edu.pe.grupo12.deliveryec4.R
import idat.edu.pe.grupo12.deliveryec4.adapters.CategoriesAdapter
import idat.edu.pe.grupo12.deliveryec4.model.Category
import idat.edu.pe.grupo12.deliveryec4.model.Usuario
import idat.edu.pe.grupo12.deliveryec4.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ClientCategoriesFragment : Fragment() {

    val TAG="CategoriesFragment"

    var myView: View? = null
    var recyclerViewCategories: RecyclerView? = null
    var categoriesProvider: CategoriesProvider? = null
    var adapter: CategoriesAdapter?= null
    var user: Usuario? = null
    var sharedPref: SharedPref? = null
    var categories = ArrayList<Category>()
    var toolbar : Toolbar? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView =  inflater.inflate(R.layout.fragment_client_categories, container, false)


//DISEÑO DE TITULO EN LA BARRA CATEGORIA
        toolbar = myView?.findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(requireContext(),R.color.white)) //COLOR
        toolbar?.title = "CATEGORIAS - La lucha" //MENSAJE
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//FIN DISEÑO


        recyclerViewCategories = myView?.findViewById(R.id.recycleview_categories)
        recyclerViewCategories?.layoutManager = LinearLayoutManager(requireContext()) //Significa que los elementos se mostraran en vertical

        sharedPref = SharedPref(requireActivity())

        getUserFromSession()

        categoriesProvider = CategoriesProvider(user?.sessionToken!!)

        getCategories()

        return myView

    }

    private fun getCategories(){
        categoriesProvider?.getAll()?.enqueue(object: Callback<ArrayList<Category>> {
            override fun onResponse(call: Call<ArrayList<Category>>, response: Response<ArrayList<Category>>
            ) {
                if (response.body() != null) {
                    categories = response.body()!!
                    adapter = CategoriesAdapter(requireActivity(), categories)
                    recyclerViewCategories?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(requireContext(),"Error ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getUserFromSession(){
        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), Usuario::class.java)

        }
    }

}