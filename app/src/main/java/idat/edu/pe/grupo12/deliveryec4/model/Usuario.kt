package idat.edu.pe.grupo12.deliveryec4.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Usuario(
    @SerializedName("id") val id:String? = null,
    @SerializedName("nombre") var nombre:String,
    @SerializedName("apellido") var apellido:String,
    @SerializedName("correo") val correo:String,
    @SerializedName("telefono") var telefono:String,
    @SerializedName("password") val password:String,
    @SerializedName("imagen") var imagen:String? = null,
    @SerializedName("session_token") val sessionToken:String? = null,
    @SerializedName("esta_disponible") val isAvailable:Boolean? = null,
    @SerializedName("roles") val roles: ArrayList<Rol>? = null
) {

    override fun toString(): String {
        return "Usuario(id=$id, nombre='$nombre', apellido='$apellido', correo='$correo', telefono='$telefono', password='$password', imagen=$imagen, sessionToken=$sessionToken, isAvailable=$isAvailable, roles=$roles)"
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }

}