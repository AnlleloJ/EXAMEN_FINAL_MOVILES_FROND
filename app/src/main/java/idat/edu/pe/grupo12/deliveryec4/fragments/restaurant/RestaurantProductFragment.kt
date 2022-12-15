package idat.edu.pe.grupo12.deliveryec4.fragments.restaurant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import idat.edu.pe.grupo12.deliveryec4.Proveedores.CategoriesProvider
import idat.edu.pe.grupo12.deliveryec4.Proveedores.ProductsProvider
import idat.edu.pe.grupo12.deliveryec4.R
import idat.edu.pe.grupo12.deliveryec4.adapters.CategoriesAdapter
import idat.edu.pe.grupo12.deliveryec4.model.Category
import idat.edu.pe.grupo12.deliveryec4.model.Product
import idat.edu.pe.grupo12.deliveryec4.model.ResponseHttp
import idat.edu.pe.grupo12.deliveryec4.model.Usuario
import idat.edu.pe.grupo12.deliveryec4.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RestaurantProductFragment : Fragment() {

    var TAG = "ProductFragment"
    var myView: View? = null
    var editTextName: EditText? = null
    var editTextDescription: EditText? = null
    var editTextPrice: EditText? = null
    var imageViewProduct1: ImageView? = null
    var imageViewProduct2: ImageView? = null
    var imageViewProduct3: ImageView? = null
    var buttonCreate: Button? = null
    var spinnerCategories: Spinner? = null

    var imageFile1: File? = null
    var imageFile2: File? = null
    var imageFile3: File? = null

    var categoriesProvider: CategoriesProvider? = null
    var productsProvider: ProductsProvider? = null
    var user: Usuario? = null
    var sharedPref: SharedPref? = null
    var categories = ArrayList<Category>()
    var idCategory = ""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView= inflater.inflate(R.layout.fragment_restaurant_product, container, false)

        editTextName = myView?.findViewById(R.id.editText_name)
        editTextDescription = myView?.findViewById(R.id.editText_description)
        editTextPrice = myView?.findViewById(R.id.editText_price)
        imageViewProduct1 = myView?.findViewById(R.id.imageview_image1)
        imageViewProduct2 = myView?.findViewById(R.id.imageview_image2)
        imageViewProduct3 = myView?.findViewById(R.id.imageview_image3)
        buttonCreate = myView?.findViewById(R.id.btn_crear)
        spinnerCategories = myView?.findViewById(R.id.spinner_categories)

        buttonCreate?.setOnClickListener { createProduct() }
        imageViewProduct1?.setOnClickListener { selectImage(101) }
        imageViewProduct2?.setOnClickListener { selectImage(102) }
        imageViewProduct3?.setOnClickListener { selectImage(103) }

        sharedPref = SharedPref(requireActivity())

        getUserFromSession()

        categoriesProvider = CategoriesProvider(user?.sessionToken!!)
        productsProvider = ProductsProvider(user?.sessionToken!!)
        getCategories()
        return myView
    }
    //----------------------------------------------------------

    private fun getCategories(){
        categoriesProvider?.getAll()?.enqueue(object: Callback<ArrayList<Category>> {
            override fun onResponse(call: Call<ArrayList<Category>>, response: Response<ArrayList<Category>>
            ) {
                if (response.body() != null) {
                    categories = response.body()!!
                    val arrayAdapter = ArrayAdapter<Category>(requireActivity(), android.R.layout.simple_dropdown_item_1line, categories)
                    spinnerCategories?.adapter = arrayAdapter
                    spinnerCategories?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            l: Long) {
                            idCategory = categories[position].id!!
                            Log.d(TAG, "id category: $idCategory")
                        }
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(requireContext(),"Error ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }
//---------------------------------------------------------------
    private fun getUserFromSession(){
        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), Usuario::class.java)

        }
    }

    //-----------------------------------------------------
    private fun createProduct(){
        val nombre = editTextName?.text.toString()
        val descripcion = editTextDescription?.text.toString()
        val precioText = editTextPrice?.text.toString()
        val files = ArrayList<File>()

        if (isValidForm(nombre,descripcion,precioText)){
            val product = Product(
                nombre = nombre,
                descripcion = descripcion,
                precio = precioText.toDouble(),
                idCategoria = idCategory
            )

            files.add(imageFile1!!)
            files.add(imageFile2!!)
            files.add(imageFile3!!)




            productsProvider?.create(files, product)?.enqueue(object : Callback<ResponseHttp>{
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {


                    Log.d(TAG, "Response: ${response}")
                    Log.d(TAG, "Body: ${response.body()}")
                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()

                    //aqui jalamos el metodo para limpiar el formulario luego del registro
                    if (response.body()?.isSuccess==true){
                        resetForm()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {


                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }
    }


    //Metodo para limpiar formulario de creación de producto luego de realizar el primer registro
    private fun resetForm(){
        editTextName?.setText("")
        editTextDescription?.setText("")
        editTextPrice?.setText("")
        imageFile1 = null
        imageFile2 = null
        imageFile3 = null
        imageViewProduct1?.setImageResource(R.drawable.ic_image)
        imageViewProduct2?.setImageResource(R.drawable.ic_image)
        imageViewProduct3?.setImageResource(R.drawable.ic_image)
    }

    private fun isValidForm(nombre: String, descripcion:String, precio:String): Boolean {

        if (nombre.isNullOrBlank()){
            Toast.makeText(requireContext(), "Ingrese el nombre del producto.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (descripcion.isNullOrBlank()){
            Toast.makeText(requireContext(), "Ingrese la descripción del producto.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (precio.isNullOrBlank()){
            Toast.makeText(requireContext(), "Ingrese el precio del producto.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (imageFile1 == null){
            Toast.makeText(requireContext(), "Ingrese la imagen 1", Toast.LENGTH_SHORT).show()
            return false
        }
        if (imageFile2 == null){
            Toast.makeText(requireContext(), "Ingrese la imagen 2", Toast.LENGTH_SHORT).show()
            return false
        }
        if (imageFile3 == null){
            Toast.makeText(requireContext(), "Ingrese la imagen 3", Toast.LENGTH_SHORT).show()
            return false
        }
        if (idCategory.isNullOrBlank()){
            Toast.makeText(requireContext(), "Selecciona la categoría del producto.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data

            if (requestCode== 101){
                imageFile1 = File(fileUri?.path)
                imageViewProduct1?.setImageURI(fileUri)
            }
            else if (requestCode== 102){
                imageFile2 = File(fileUri?.path)
                imageViewProduct2?.setImageURI(fileUri)
            }
            else if (requestCode== 103){
                imageFile3 = File(fileUri?.path)
                imageViewProduct3?.setImageURI(fileUri)
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }


    //metodo para seleccionar imagen de galeria o tomar foto
    private  fun selectImage(requestCode: Int){
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .start(requestCode)

    }

}