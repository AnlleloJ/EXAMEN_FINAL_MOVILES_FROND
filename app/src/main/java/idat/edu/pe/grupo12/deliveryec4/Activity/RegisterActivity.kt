package idat.edu.pe.grupo12.deliveryec4.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.Gson
import idat.edu.pe.grupo12.deliveryec4.Activity.client.home.ClientHomeActivity
import idat.edu.pe.grupo12.deliveryec4.Proveedores.UsuarioProveedor
import idat.edu.pe.grupo12.deliveryec4.R
import idat.edu.pe.grupo12.deliveryec4.model.ResponseHttp
import idat.edu.pe.grupo12.deliveryec4.model.Usuario
import idat.edu.pe.grupo12.deliveryec4.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    val TAG = "RegisterActivity"

    var imageViewGoToLogin: ImageView? = null
    var editTextNombre: EditText? = null
    var editTextApellido: EditText? = null
    var editTextCorreo: EditText? = null
    var editTextTelefono: EditText? = null
    var editTextPassword: EditText? = null
    var editTextConfirmPassword: EditText? = null
    var buttonRegister: Button? = null

    var usersProvider = UsuarioProveedor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)


        imageViewGoToLogin = findViewById(R.id.imageview_go_to_login)
        editTextNombre = findViewById(R.id.edit_text_nombre)
        editTextApellido = findViewById(R.id.edit_text_apellido)
        editTextCorreo = findViewById(R.id.edit_text_correo_registro)
        editTextTelefono = findViewById(R.id.edit_text_telefono)
        editTextPassword = findViewById(R.id.edit_text_password_registro)
        editTextConfirmPassword = findViewById(R.id.edit_text_repassword_registro)
        buttonRegister = findViewById(R.id.btn_registro)


        imageViewGoToLogin?.setOnClickListener{ irLogin() }
        buttonRegister?.setOnClickListener{ registro()}
    }

    private fun registro(){
        val nombre=editTextNombre?.text.toString()
        val apellido=editTextApellido?.text.toString()
        val correo=editTextCorreo?.text.toString()
        val telefono=editTextTelefono?.text.toString()
        val password=editTextPassword?.text.toString()
        val confirmpassword=editTextConfirmPassword?.text.toString()

        //Validamos el formulario

        if (validarFormulario(nombre = nombre, apellido=apellido, telefono=telefono, correo=correo, password=password, confirmpassword=confirmpassword)){

            val usuario = Usuario(

                nombre = nombre,
                apellido = apellido,
                correo = correo,
                telefono = telefono,
                password = password
            )
            usersProvider.register(usuario)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    if(response.body()?.isSuccess == true){
                        saveUserInSession(response.body()?.data.toString())
                        goToClientHome()
                    }

                    Toast.makeText(this@RegisterActivity, response.body()?.message, Toast.LENGTH_LONG).show()

                    Log.d(TAG,  "Response: ${response}")
                    Log.d(TAG,  "Response: ${response.body()}")
                }

                override fun onFailure(p0: Call<ResponseHttp>, t: Throwable) {


                    Log.d(TAG,"Se produjo un error ${t.message}")
                    Toast.makeText(this@RegisterActivity, " Se produjo un error ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }


        /*
        Log.d(TAG,"El nombre es: $nombre")
        Log.d(TAG,"El Apellido es: $apellido")
        Log.d(TAG,"El correo es: $correo")
        Log.d(TAG,"El teléfono es: $telefono")
        Log.d(TAG,"La contraseña es: $password")
        Log.d(TAG,"La confirmación es: $confirmpassword")
         */
    }


    private fun goToClientHome(){
        val i = Intent(this, SaveImageActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //METODO PARA BORRAR HISTORIAL PANTALLAS
        startActivity(i)
    }

    private fun saveUserInSession(data:String){
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, Usuario::class.java)
        sharedPref.save("user",user)

    }
    //METODO COPIADO DEL MAIN ACTIVITY
    //INICIO METODO VALIDACION
    //VALIDAR E-MAIL
    private fun String.isEmailValid():Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    //VALIDAR FORMULARIO
    private fun validarFormulario(
        nombre:String,
        apellido:String,
        correo:String,
        telefono:String,
        password:String,
        confirmpassword:String
    ): Boolean {
        if (nombre.isBlank()){
            Toast.makeText(this,"Debe ingresar su nombre.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (apellido.isBlank()){
            Toast.makeText(this,"Debe ingresar sus apellidos.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (correo.isBlank()){
            Toast.makeText(this,"Debe ingresar su E-mail.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (telefono.isBlank()){
            Toast.makeText(this,"Debe ingresar su número telefónico.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isBlank()){
            Toast.makeText(this,"Debe ingresar su contraseña.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (confirmpassword.isBlank()){
            Toast.makeText(this,"Debe confirmar su contraseña.", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!correo.isEmailValid()){
            Toast.makeText(this,"El E-mail no es valido.", Toast.LENGTH_SHORT).show()
            return false
        }
        //confirmar la contraseña
        if (password != confirmpassword){
            Toast.makeText(this,"La contraseña no coincide.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true

    }
    //FIN METODO VALIDACIÓN

    private fun irLogin() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}