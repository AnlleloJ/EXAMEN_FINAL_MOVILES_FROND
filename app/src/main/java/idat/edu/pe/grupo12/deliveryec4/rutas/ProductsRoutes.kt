package idat.edu.pe.grupo12.deliveryec4.rutas

import idat.edu.pe.grupo12.deliveryec4.model.Category
import idat.edu.pe.grupo12.deliveryec4.model.Product
import idat.edu.pe.grupo12.deliveryec4.model.ResponseHttp
import idat.edu.pe.grupo12.deliveryec4.model.Usuario
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProductsRoutes {

    //obtener
    @GET("products/findByCategory/{id_categoria}")
    fun findByCategory (
        @Path("id_categoria") idCategory: String,
        @Header("Authorization") token: String
    ): Call<ArrayList<Product>>

    //añadir
    @Multipart
    @POST ("products/create")//creación de productos
    fun create(
        @Part images: Array<MultipartBody.Part?>,
        @Part("product") product: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>



}