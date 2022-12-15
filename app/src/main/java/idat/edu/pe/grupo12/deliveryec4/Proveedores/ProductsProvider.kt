package idat.edu.pe.grupo12.deliveryec4.Proveedores

import android.util.Log
import idat.edu.pe.grupo12.deliveryec4.api.apiRutas
import idat.edu.pe.grupo12.deliveryec4.model.Category
import idat.edu.pe.grupo12.deliveryec4.model.Product
import idat.edu.pe.grupo12.deliveryec4.model.ResponseHttp
import idat.edu.pe.grupo12.deliveryec4.model.Usuario
import idat.edu.pe.grupo12.deliveryec4.rutas.CategoriesRoutes
import idat.edu.pe.grupo12.deliveryec4.rutas.ProductsRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class ProductsProvider(val token: String) {

    private var productsRoutes: ProductsRoutes? = null

    init {
        val api= apiRutas()
        productsRoutes = api.getProductsRoutes(token)
    }

    fun findByCategory(idCategory: String): Call<ArrayList<Product>>? {
        return productsRoutes?.findByCategory(idCategory, token)
    }

    fun create(files: List<File>, product: Product): Call<ResponseHttp>? {

        val images = arrayOfNulls<MultipartBody.Part>(files.size)

        for (i in 0 until files.size) {
            val reqFile = RequestBody.create(MediaType.parse("image/*"),  files[i])
            images [i]= MultipartBody.Part.createFormData("image", files[i].name, reqFile)
        }

        val requestBody = RequestBody.create(MediaType.parse("Text/plain"), product.toJson())

        return productsRoutes?.create(images ,requestBody, token)
    }






}