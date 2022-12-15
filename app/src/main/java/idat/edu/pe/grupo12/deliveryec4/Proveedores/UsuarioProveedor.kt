package idat.edu.pe.grupo12.deliveryec4.Proveedores

import idat.edu.pe.grupo12.deliveryec4.api.apiRutas
import idat.edu.pe.grupo12.deliveryec4.model.ResponseHttp
import idat.edu.pe.grupo12.deliveryec4.model.Usuario
import idat.edu.pe.grupo12.deliveryec4.rutas.RutaUsuario
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class UsuarioProveedor (val token : String? = null){

    private var usersRoutes: RutaUsuario? = null
    private var usersRoutesToken: RutaUsuario? = null

    //metodo que se ejecuta primero antes que la clase
    init {
        val api = apiRutas()
        usersRoutes = api.getUsersRoutes()

        if(token !=null){
            usersRoutesToken = api.getUserRoutesWithToken(token!!)
        }

    }
    //estamos jalando datos del archivo ruta usuario
    fun register( usuario: Usuario): Call<ResponseHttp>? {
        return usersRoutes?.registro(usuario)
    }

    fun login( correo: String, password: String): Call<ResponseHttp>? {
        return usersRoutes?.login(correo,password)
    }

    fun updatewithoutImage( user: Usuario): Call<ResponseHttp>? {
        return usersRoutesToken?.updatewithoutImage(user, token!!)
    }

    fun update(file: File, user: Usuario): Call<ResponseHttp>? {
        val reqFile = RequestBody.create(MediaType.parse("image/*"),  file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("Text/plain"), user.toJson())

        return usersRoutesToken?.update(image ,requestBody, token!!)
    }
}