package idat.edu.pe.grupo12.deliveryec4.fragments.restaurant

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import idat.edu.pe.grupo12.deliveryec4.Proveedores.CategoriesProvider
import idat.edu.pe.grupo12.deliveryec4.R
import idat.edu.pe.grupo12.deliveryec4.model.Category
import idat.edu.pe.grupo12.deliveryec4.model.ResponseHttp
import idat.edu.pe.grupo12.deliveryec4.model.Usuario
import idat.edu.pe.grupo12.deliveryec4.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class RestaurantCategoryFragment : Fragment() {

    val TAG = "CategoryFragment"

    var myView: View? = null
    var imageViewCategory: ImageView? = null
    var editTextCategory: EditText? = null
    var buttonCreate: Button? = null

    private var imageFile: File? = null

    var categoriesProvider: CategoriesProvider? = null
    var sharedPref: SharedPref? = null
    var user: Usuario? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_restaurant_category, container, false)

        sharedPref = SharedPref(requireActivity())


        imageViewCategory = myView?.findViewById(R.id.imageview_categoryRestaurant)
        editTextCategory = myView?.findViewById(R.id.editText_restaurant_category)
        buttonCreate = myView?.findViewById(R.id.btn_crearCategoria_restaurant)

        imageViewCategory?.setOnClickListener { selectImage() }
        buttonCreate?.setOnClickListener {  createCategory()}

        getUserFromSession()
        categoriesProvider = CategoriesProvider(user?.sessionToken!!)
        return  myView
    }

    //-------------------------------------

    private fun getUserFromSession(){
        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), Usuario::class.java)

        }
    }
    //-------------------------------------

    private fun createCategory() {
        val name = editTextCategory?.text.toString()

        if (imageFile != null) {

            val category = Category( nombre =  name)

            categoriesProvider?.create(imageFile!!, category)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {


                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")

                    Toast.makeText(requireContext(),response.body()?.message, Toast.LENGTH_LONG).show()

                    if (response.body()?.isSuccess == true) {
                        clearForm()
                    }

                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })

        }
        else {
            Toast.makeText(requireContext(), "Selecciona una imagen.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearForm() {
        editTextCategory?.setText("")
        imageFile = null
        imageViewCategory?.setImageResource(R.drawable.ic_image)
    }

    private val startImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->

        val resultCode = result.resultCode
        val data = result.data


        //Si el usuario selecciona una imagen
        if (resultCode== Activity.RESULT_OK){
            val fileUri = data?.data
            imageFile = File(fileUri?.path) //Es el archivo que vamos a guardar en el almacen/servidor/storage
            //luego de validar la selección, insertamos la imagen en nuestro circleimage
            imageViewCategory?.setImageURI(fileUri)
        }
        else if (resultCode== ImagePicker.RESULT_ERROR){
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(requireContext(), "La tarea se canceló.", Toast.LENGTH_LONG).show()
        }

    }
    //metodo para seleccionar imagen de galeria o tomar foto
    private  fun selectImage(){
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent { intent ->
                startImageForResult.launch(intent)

            }

    }


}