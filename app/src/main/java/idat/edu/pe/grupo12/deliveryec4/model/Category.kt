package idat.edu.pe.grupo12.deliveryec4.model

import com.google.gson.Gson

class Category (
    val id: String? = null,
    val nombre: String,
    val imagen: String? = null,
){
    override fun toString(): String {
        return nombre
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
}