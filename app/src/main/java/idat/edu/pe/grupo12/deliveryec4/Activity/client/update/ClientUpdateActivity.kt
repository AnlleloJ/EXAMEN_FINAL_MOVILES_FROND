package idat.edu.pe.grupo12.deliveryec4.Activity.client.update

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import idat.edu.pe.grupo12.deliveryec4.R
import idat.edu.pe.grupo12.deliveryec4.model.ResponseHttp
import idat.edu.pe.grupo12.deliveryec4.model.Usuario
import idat.edu.pe.grupo12.deliveryec4.Proveedores.UsuarioProveedor
import idat.edu.pe.grupo12.deliveryec4.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ClientUpdateActivity : AppCompatActivity() {

    val TAG = "ClientUpdateActivity"

    var circleImageUser: CircleImageView? = null
    var editTextName:EditText? = null
    var editTextLastname: EditText? = null
    var editTextPhone: EditText? = null
    var buttonUpdate: Button? = null

    var sharedPref: SharedPref? = null
    var user: Usuario? = null

    private var imageFile: File? = null
    var usersProvider: UsuarioProveedor? = null

    var toolbar: Toolbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_update)

        sharedPref = SharedPref(this)
//--------------------------------------------------------------------
        //EDICION BARRA TITULO
        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this ,R.color.white)) //COLOR
        toolbar?.title = "EDITAR PERFIL" //MENSAJE
        setSupportActionBar(toolbar)
        //FIN EDICION
//-------------------------------------------------------------------
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //Con esto volveremos al formulario anterior, nos permite usar una flecha y nos envia al parent activity


        circleImageUser = findViewById(R.id.circleimage_user)
        editTextName = findViewById(R.id.edit_text_nombre_update)
        editTextLastname = findViewById(R.id.edit_text_apellido_update)
        editTextPhone = findViewById(R.id.edit_text_telefono_update)
        buttonUpdate = findViewById(R.id.btn_update)

        getUserFromSession()

        usersProvider = UsuarioProveedor(user?.sessionToken)

        editTextName?.setText(user?.nombre)
        editTextLastname?.setText(user?.apellido)
        editTextPhone?.setText(user?.telefono)

        //esta validacion nos sirve para verificar que el usuario tenga una url en el campo imagen
        if(!user?.imagen.isNullOrBlank()){
            Glide.with(this).load(user?.imagen).into(circleImageUser!!)
        }
        circleImageUser?.setOnClickListener { selectImage() }
        buttonUpdate?.setOnClickListener {  updateData()}


    }

    //metodo copiado de saveimage
    private fun updateData(){

        val nombre=editTextName?.text.toString()
        val apellido=editTextLastname?.text.toString()
        val telefono=editTextPhone?.text.toString()

        user?.nombre = nombre
        user?.apellido = apellido
        user?.telefono = telefono

        if (imageFile != null) {
            usersProvider?.update(imageFile!!, user!!)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {


                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")

                    Toast.makeText(this@ClientUpdateActivity, response.body()?.message, Toast.LENGTH_SHORT).show()

                    if (response.body()?.isSuccess == true) {
                        saveUserInSession(response.body()?.data.toString())
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(this@ClientUpdateActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
        else {
            usersProvider?.updatewithoutImage(user!!)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")

                    Toast.makeText(this@ClientUpdateActivity, response.body()?.message, Toast.LENGTH_SHORT).show()

                    if (response.body()?.isSuccess == true) {
                        saveUserInSession(response.body()?.data.toString())
                    }
                    saveUserInSession(response.body()?.data.toString())
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(this@ClientUpdateActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })

        }



    }
//copiado del activity saveImage
    private fun saveUserInSession(data: String) {
        val gson = Gson()
        val user = gson.fromJson(data, Usuario::class.java)
        sharedPref?.save("user", user)
    }

    private fun getUserFromSession(){
        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), Usuario::class.java)

        }
    }

    //copiado del activity saveImage
    private val startImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->

        val resultCode = result.resultCode
        val data = result.data
        //Si el usuario selecciona una imagen

        if (resultCode== Activity.RESULT_OK){
            val fileUri = data?.data
            imageFile = File(fileUri?.path) //Es el archivo que vamos a guardar en el almacen/servidor/storage
            //luego de validar la selección, insertamos la imagen en nuestro circleimage
            circleImageUser?.setImageURI(fileUri)
        }
        else if (resultCode== ImagePicker.RESULT_ERROR){
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(this, "Tarea se canceló", Toast.LENGTH_LONG).show()
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