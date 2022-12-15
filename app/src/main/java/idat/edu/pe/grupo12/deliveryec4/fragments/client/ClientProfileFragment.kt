package idat.edu.pe.grupo12.deliveryec4.fragments.client

import android.content.Intent
import android.os.Bundle
import android.os.UserHandle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import idat.edu.pe.grupo12.deliveryec4.Activity.MainActivity
import idat.edu.pe.grupo12.deliveryec4.Activity.SelectRolesActivity
import idat.edu.pe.grupo12.deliveryec4.Activity.client.update.ClientUpdateActivity
import idat.edu.pe.grupo12.deliveryec4.R
import idat.edu.pe.grupo12.deliveryec4.model.Usuario
import idat.edu.pe.grupo12.deliveryec4.utils.SharedPref


class ClientProfileFragment : Fragment() {

    var myView: View? = null
    var buttonSelectRol: Button? = null
    var buttonUpdateProfile: Button? = null
    var circleImageUser: CircleImageView? = null
    var textViewName: TextView? = null
    var textViewEmail: TextView? = null
    var textViewPhone: TextView? = null
    var imageViewLogout: ImageView? = null

    var sharedPref: SharedPref? = null
    var user : Usuario? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView=  inflater.inflate(R.layout.fragment_client_profile, container, false)

        sharedPref = SharedPref(requireActivity())

        buttonSelectRol = myView?.findViewById(R.id.btn_select_rol)
        buttonUpdateProfile = myView?.findViewById(R.id.btn_update_profile)
        textViewName = myView?.findViewById(R.id.textview_name)
        textViewEmail = myView?.findViewById(R.id.textview_email)
        textViewPhone = myView?.findViewById(R.id.textview_phone)
        circleImageUser = myView?.findViewById(R.id.circleImage_user)
        imageViewLogout = myView?.findViewById(R.id.imageview_logout)


        buttonSelectRol?.setOnClickListener { goToSelectRol()  }
        imageViewLogout?.setOnClickListener { logout()  }
        buttonUpdateProfile?.setOnClickListener { goToUpdate() }

        getUserFromSession()
        textViewName?.text ="${user?.nombre} ${user?.apellido}"
        textViewEmail?.text = user?.correo
        textViewPhone?.text = user?.telefono

        if (!user?.imagen.isNullOrBlank()){
            Glide.with(requireContext()).load(user?.imagen).into(circleImageUser!!)
        }

        return myView
    }

    private fun logout (){
        sharedPref?.remove("user")
        val i = Intent(requireContext(), MainActivity::class.java)
        startActivity(i)

    }

    private fun getUserFromSession(){
        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
             user = gson.fromJson(sharedPref?.getData("user"), Usuario::class.java)

        }
    }

    private fun goToUpdate(){
        val i = Intent(requireContext(), ClientUpdateActivity::class.java)
        startActivity(i)
    }


    private fun goToSelectRol(){
        val i = Intent(requireContext(), SelectRolesActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //METODO PARA BORRAR HISTORIAL PANTALLAS
        startActivity(i)
    }

}