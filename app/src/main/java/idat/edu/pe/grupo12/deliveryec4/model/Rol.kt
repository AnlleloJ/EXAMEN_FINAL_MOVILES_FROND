package idat.edu.pe.grupo12.deliveryec4.model

import com.google.gson.annotations.SerializedName

class Rol (

    @SerializedName("id") val id:String,
    @SerializedName("nombre") val nombre:String,
    @SerializedName("imagen") val imagen:String,
    @SerializedName("ruta") val ruta:String,
){

    override fun toString(): String {
        return "Rol(id='$id', nombre='$nombre', imagen='$imagen', ruta='$ruta')"
    }
}