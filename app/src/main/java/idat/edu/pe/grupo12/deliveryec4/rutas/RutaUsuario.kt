package idat.edu.pe.grupo12.deliveryec4.rutas

import idat.edu.pe.grupo12.deliveryec4.model.ResponseHttp
import idat.edu.pe.grupo12.deliveryec4.model.Usuario
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface RutaUsuario {
    @POST(value = "users/create")
    fun registro(@Body usuario:Usuario): Call<ResponseHttp>

    @FormUrlEncoded
    @POST(value = "users/login")
    fun login(@Field("correo") correo:String, @Field("password") password:String): Call<ResponseHttp>

    @Multipart
    @PUT ("users/update")//actualizacion de datos
    fun update(
        @Part image: MultipartBody.Part,
        @Part("user") user: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @PUT ("users/updatewithoutImage")//actualizacion de datos
    fun updatewithoutImage(
        @Body user: Usuario,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>



}