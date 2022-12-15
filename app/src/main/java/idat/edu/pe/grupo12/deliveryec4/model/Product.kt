package idat.edu.pe.grupo12.deliveryec4.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Product(
    @SerializedName("id")val id : String? = null,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("imagen1") val imagen1: String? = null,
    @SerializedName("imagen2") val imagen2: String? = null,
    @SerializedName("imagen3") val imagen3: String? = null,
    @SerializedName("id_categoria") val idCategoria: String,
    @SerializedName("precio") val precio: Double,
    @SerializedName("cantidad") var cantidad: Int? = null,
) {

    fun toJson(): String{
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "Product(id='$id', nombre='$nombre', descripcion='$descripcion', imagen1='$imagen1', imagen2='$imagen2', imagen3='$imagen3', idCaregoria='$idCategoria', price=$precio, cantidad=$cantidad)"
    }


}