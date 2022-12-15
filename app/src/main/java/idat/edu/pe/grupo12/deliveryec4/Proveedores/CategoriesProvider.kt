package idat.edu.pe.grupo12.deliveryec4.Proveedores

import android.util.Log
import idat.edu.pe.grupo12.deliveryec4.api.apiRutas
import idat.edu.pe.grupo12.deliveryec4.model.Category
import idat.edu.pe.grupo12.deliveryec4.model.ResponseHttp
import idat.edu.pe.grupo12.deliveryec4.model.Usuario
import idat.edu.pe.grupo12.deliveryec4.rutas.CategoriesRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class CategoriesProvider(val token: String) {

    private var categoriesRoutes: CategoriesRoutes? = null

    init {
        val api= apiRutas()
        categoriesRoutes = api.getCategoriesRoutes(token)
    }

    fun getAll(): Call<ArrayList<Category>>? {
        return categoriesRoutes?.getAll(token)
    }

    fun create(file: File, category: Category): Call<ResponseHttp>? {
        val reqFile = RequestBody.create(MediaType.parse("image/*"),  file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)

        Log.d("CATEGORY", category.toJson())


        val requestBody = RequestBody.create(MediaType.parse("Text/plain"), category.toJson())

        return categoriesRoutes?.create(image ,requestBody, token)
    }






}