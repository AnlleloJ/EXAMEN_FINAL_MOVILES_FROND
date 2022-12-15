package idat.edu.pe.grupo12.deliveryec4.api

import idat.edu.pe.grupo12.deliveryec4.rutas.CategoriesRoutes
import idat.edu.pe.grupo12.deliveryec4.rutas.ProductsRoutes
import retrofit2.Retrofit
import idat.edu.pe.grupo12.deliveryec4.rutas.RutaUsuario

class apiRutas {
    //verificar que la dirección IP coincida con la dirección UPv4
    val API_URL = "http://192.168.1.28:3000/api/"
    val retrofit = RetrofitClient()

    fun getUsersRoutes(): RutaUsuario {
        return retrofit.getClient(API_URL).create(RutaUsuario::class.java)
    }

    fun getUserRoutesWithToken(token: String): RutaUsuario {
        return retrofit.getClientWithToken(API_URL, token).create(RutaUsuario::class.java)
    }

    fun getCategoriesRoutes(token: String): CategoriesRoutes {
        return retrofit.getClientWithToken(API_URL, token).create(CategoriesRoutes::class.java)
    }

    fun getProductsRoutes(token: String): ProductsRoutes {
        return retrofit.getClientWithToken(API_URL, token).create(ProductsRoutes::class.java)
    }



}