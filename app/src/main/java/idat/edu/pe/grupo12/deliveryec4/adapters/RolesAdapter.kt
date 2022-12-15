package idat.edu.pe.grupo12.deliveryec4.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import idat.edu.pe.grupo12.deliveryec4.Activity.client.home.ClientHomeActivity
import idat.edu.pe.grupo12.deliveryec4.Activity.restaurant.home.RestaurantHomeActivity
import idat.edu.pe.grupo12.deliveryec4.R
import idat.edu.pe.grupo12.deliveryec4.model.Rol
import idat.edu.pe.grupo12.deliveryec4.utils.SharedPref

class RolesAdapter (val context: Activity, val roles: ArrayList<Rol>): RecyclerView.Adapter<RolesAdapter.RolesViewHolder>(){

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): RolesViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_roles, parent, false)
        return RolesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roles.size
    }

    //metodo para especificar valores de vista
    override fun onBindViewHolder(holder: RolesViewHolder, position: Int){

        val rol = roles[position]//cada rol

        holder.textViewRol.text = rol.nombre
        Glide.with(context).load(rol.imagen).into(holder.imageViewRol)

        holder.itemView.setOnClickListener {  goToRol(rol)}
    }
    //Rutas de cada rol seleccionado
    private fun goToRol(rol: Rol){
        if (rol.nombre == "ADMINISTRADOR"){

            sharedPref.save("rol", "ADMINISTRADOR")


            val i = Intent(context, RestaurantHomeActivity::class.java)
            context.startActivity(i)
        }
        else if (rol.nombre == "CLIENTE"){
            sharedPref.save("rol", "CLIENTE")

            val i = Intent(context, ClientHomeActivity::class.java)
            context.startActivity(i)
        }
    }


    class RolesViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewRol : TextView
        val imageViewRol: ImageView

        init {
            textViewRol = view.findViewById(R.id.textview_rol)
            imageViewRol = view.findViewById(R.id.imageview_rol)
        }
    }
}