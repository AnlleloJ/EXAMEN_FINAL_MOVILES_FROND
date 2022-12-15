package idat.edu.pe.grupo12.deliveryec4.Activity.client.products.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import idat.edu.pe.grupo12.deliveryec4.R
import idat.edu.pe.grupo12.deliveryec4.model.Product

class ClientProductsDetailActivity : AppCompatActivity() {

    var product: Product? = null
    val gson = Gson()

    var imageSlider: ImageSlider? = null
    var textViewName: TextView? = null
    var textViewDescription: TextView? = null
    var textViewPrice: TextView? = null
    var textViewCounter: TextView? = null
    var imageViewAdd: ImageView? = null
    var imageViewRemove: ImageView? = null
    var buttonAdd: Button?= null


    var counter = 1
    var productPrice = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_detail)

        product = gson.fromJson(intent.getStringExtra("product"), Product::class.java)

        imageSlider = findViewById(R.id.imageslider)
        textViewName = findViewById(R.id.textView_productdetail_name)
        textViewDescription = findViewById(R.id.textview_productDetail_descripcion)
        textViewPrice = findViewById(R.id.textView_price_productdetail)
        textViewCounter = findViewById(R.id.textView_Counter)
        imageViewAdd = findViewById(R.id.imageview_more)
        imageViewRemove = findViewById(R.id.imageview_clear)
       // buttonAdd = findViewById(R.id.btn_addProduct)

        val imageList = java.util.ArrayList<SlideModel>()
        imageList.add(SlideModel(product?.imagen1, ScaleTypes.CENTER_CROP)) //permite que la imagen se expanda
        imageList.add(SlideModel(product?.imagen2, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.imagen3, ScaleTypes.CENTER_CROP))

        imageSlider?.setImageList(imageList)

        textViewName?.text = product?.nombre
        textViewDescription?.text = product?.descripcion
        textViewPrice?.text = "S/.${product?.precio}"


        imageViewAdd?.setOnClickListener { addItem() }
        imageViewRemove?.setOnClickListener { removeItem() }
    }

    //Los siguientes metodos los usamos para añadir accion al los botones de + y - en el formulario productDetail
    //añadir
    private fun addItem(){
        counter++
        productPrice = product?.precio!! * counter

        product?.cantidad = counter

        textViewCounter?.text = "${product?.cantidad}"
        textViewPrice?.text = "S/.${productPrice}"
    }
    //eliminar
    private fun removeItem(){
        if (counter >1){
            counter--
            productPrice = product?.precio!! * counter

            product?.cantidad = counter

            textViewCounter?.text = "${product?.cantidad}"
            textViewPrice?.text = "S/.${productPrice}"
        }
    }


}