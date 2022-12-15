package idat.edu.pe.grupo12.deliveryec4.Activity

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import idat.edu.pe.grupo12.deliveryec4.Activity.client.home.ClientHomeActivity
import idat.edu.pe.grupo12.deliveryec4.R
import idat.edu.pe.grupo12.deliveryec4.model.ResponseHttp
import idat.edu.pe.grupo12.deliveryec4.model.Usuario
import idat.edu.pe.grupo12.deliveryec4.Proveedores.UsuarioProveedor
import idat.edu.pe.grupo12.deliveryec4.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SaveImageActivity : AppCompatActivity() {

    var TAG= "SaveImageActivity"

    var circleImageUser: CircleImageView? = null
    var buttonNext: Button? = null
    var buttonConfirm: Button? = null

    private var imageFile: File? = null


    var usersProvider: UsuarioProveedor? = null

    var user: Usuario? = null
    var sharedPref: SharedPref? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_image)

        sharedPref = SharedPref(this)

        getUserFromSession()

        usersProvider = UsuarioProveedor(user?.sessionToken)

        circleImageUser = findViewById(R.id.circleImage_user)
        buttonNext = findViewById(R.id.btn_next)
        buttonConfirm = findViewById(R.id.btn_confirm)


        circleImageUser?.setOnClickListener {  selectImage()}

        buttonNext?.setOnClickListener { goToClientHome() }
        buttonConfirm?.setOnClickListener { saveImage() }
    }

    private fun saveImage(){

        if(imageFile !=null && user !=null){


            usersProvider?.update(imageFile!!, user!!)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")

                    saveUserInSession(response.body()?.data.toString())
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(this@SaveImageActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
        else {
            Toast.makeText(this, "La imagen no puede ser nula, ni tampoco los datos de sesión del usuario.", Toast.LENGTH_LONG).show()
        }
    }
//metdo copiado del main
    private fun saveUserInSession(data: String) {
        val gson = Gson()
        val user = gson.fromJson(data, Usuario::class.java)
        sharedPref?.save("user", user)
        goToClientHome()
    }

    private fun goToClientHome(){
        val i = Intent(this, ClientHomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //METODO PARA BORRAR HISTORIAL PANTALLAS
        startActivity(i)
    }

    /*
    * metodo copiado de clientHomeActivity
     */
    private fun getUserFromSession(){
        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
             user = gson.fromJson(sharedPref?.getData("user"), Usuario::class.java)
        }
    }

    private val startImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: androidx.activity.result.ActivityResult ->

        val resultCode = result.resultCode
        val data = result.data
    //Si el usuario selecciona una imagen
        if (resultCode== Activity.RESULT_OK){
            val fileUri = data?.data
            imageFile = File(fileUri?.path) //Es el archivo que vamos a guardar en el almacen/servidor/storage
            //luego de validar la selección, insertamos la imagen en nuestro circleimage
            circleImageUser?.setImageURI(fileUri)
        }
        else if (resultCode==ImagePicker.RESULT_ERROR){
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