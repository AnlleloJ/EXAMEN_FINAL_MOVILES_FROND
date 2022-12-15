package idat.edu.pe.grupo12.deliveryec4.Activity

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.Gson
import idat.edu.pe.grupo12.deliveryec4.Activity.client.home.ClientHomeActivity
import idat.edu.pe.grupo12.deliveryec4.Activity.restaurant.home.RestaurantHomeActivity
import idat.edu.pe.grupo12.deliveryec4.Proveedores.UsuarioProveedor
import idat.edu.pe.grupo12.deliveryec4.R
import idat.edu.pe.grupo12.deliveryec4.model.ResponseHttp
import idat.edu.pe.grupo12.deliveryec4.model.Usuario
import idat.edu.pe.grupo12.deliveryec4.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    var imageViewGoToRegister: ImageView?=null
    var editTextEmail: EditText? = null
    var editTextPasword: EditText? = null
    var buttonLogin: Button? = null
    var usersProvider = UsuarioProveedor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        imageViewGoToRegister = findViewById(R.id.imageview_go_to_registrer)
        editTextEmail = findViewById(R.id.edit_text_email)
        editTextPasword = findViewById(R.id.edit_text_pasword)
        buttonLogin = findViewById(R.id.btn_login)

        imageViewGoToRegister?.setOnClickListener{ goToRegister()}
        buttonLogin?.setOnClickListener{ login()}

        getUserFromSession()
    }


    //Con este método ejecutamos los datos en el login del usuario
    //Nos aseguramos que los datos del formulario sean correctos.
    private fun login (){
        val email = editTextEmail?.text.toString()
        val password = editTextPasword?.text.toString()

        //Jalamos metodo validarformulario
        if(validarFormulario(email, password)){

            usersProvider.login(email,password)?.enqueue(object : Callback<ResponseHttp>{
                //metodo 1
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    Log.d("MainActivity", "Response: ${response.body()}")
                    //validación
                    if(response.body()?.isSuccess == true) {
                        Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                        saveUserInSession(response.body()?.data.toString())

                    }
                    else {
                        Toast.makeText(this@MainActivity,"Los datos son incorrectos.", Toast.LENGTH_LONG).show()
                    }
                }
                //metodo 2
                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d("MainActivity", "Hubo un error ${t.message}")
                    Toast.makeText(this@MainActivity, "Hubo un error ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
        else {
            Toast.makeText(this, "El usuario o contraseña es incorrecto.", Toast.LENGTH_LONG).show()
        }

       // Toast.makeText(this, "El E-mail es: $email", Toast.LENGTH_LONG).show()
       // Toast.makeText(this, "La contraseña es: $password", Toast.LENGTH_LONG).show()

        //Log.d("MainActivity", "El E-mail es: $email")
        //Log.d("MainActivity", "La contraseña es: $password")
    }

    //---------------------------------------------------
    //FUNCIÓN DE DIRECCION ROLES/FORMULARIOS
    //---------------------------------------------------
    private fun goToClientHome(){
        val i = Intent(this,ClientHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //METODO PARA BORRAR HISTORIAL PANTALLAS
        startActivity(i)
    }
    private fun goToRestaurantHome(){
        val i = Intent(this,RestaurantHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //METODO PARA BORRAR HISTORIAL PANTALLAS
        startActivity(i)
    }
    private fun goToSelectRol(){
        val i = Intent(this,SelectRolesActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //METODO PARA BORRAR HISTORIAL PANTALLAS
        startActivity(i)
    }

    private fun saveUserInSession(data:String){
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, Usuario::class.java)
        sharedPref.save("user",user)

        //determinamos cuantos roles tienen los usuarios
        //Por ejemplo, el primer if identifica si el usuario tiene MAS de 1 rol.
        if (user.roles?.size!! > 1){
            goToSelectRol()
        }
        else{ // Con el Else identificamos que tenga unicamente 1 rol y le asignamos un procedimiento.
            goToClientHome()
        }

    }
    //validaciòn de EMAIL - Nos aseguramos que la escritura del correo sea valido.
    private fun String.isEmailValid():Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }


    //luego de validar credenciales, usamos este metodo para iniciar directamente del formulario cliente_home.
    private fun getUserFromSession(){
        val sharedPref = SharedPref(this)
        val gson = Gson()

        if(!sharedPref.getData("user").isNullOrBlank()){
            //Si el usuario existe en sesión
            val user = gson.fromJson(sharedPref.getData("user"), Usuario::class.java)

            //Si el usuario selecciona el rol
            if(!sharedPref.getData("rol").isNullOrBlank()){
                val rol =sharedPref.getData("rol")?.replace("\"", "")
                if(rol== "RESTAURANTE"){
                    goToRestaurantHome()
                }
                else if(rol == "CLIENTE"){
                    goToClientHome()
                }
            }
            else {
                goToClientHome()
            }
        }
    }

    private fun validarFormulario(correo:String, password:String): Boolean {
        if (correo.isBlank()){
            return false
        }
        if (password.isBlank()){
            return false
        }
        if(!correo.isEmailValid()){
            return false
        }
        return true

    }



    private fun goToRegister(){
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }

}