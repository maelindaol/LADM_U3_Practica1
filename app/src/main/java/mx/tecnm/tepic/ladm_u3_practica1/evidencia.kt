package mx.tecnm.tepic.ladm_u3_practica1

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.provider.FontsContractCompat.FontRequestCallback.RESULT_OK
import kotlinx.android.synthetic.main.activity_evidencia.*
import java.io.ByteArrayOutputStream

class evidencia : AppCompatActivity() {
    private val SELECT_ACTIVITY = 50
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evidencia)
        this.setTitle("Evidencia");
        imageViewe.setImageResource(R.drawable.image)
        var extra = intent.extras
        var id = extra!!.getString("idactividad")!!
        imageViewe.setOnClickListener{
            imageController.selectPhotoFromGallery(this,SELECT_ACTIVITY)
        }
        buttonRegresar4.setOnClickListener{
            finish()
        }
        buttonInsertarEv4.setOnClickListener {
            val imagen = (imageViewe.drawable as BitmapDrawable).bitmap
            var imagenBytes = convierteBytes(imagen)
            var evidenciac = evidencia2(
                id,
                imagenBytes
            )
            evidenciac.asignarPuntero(this)
            var res = evidenciac.insertar()
            if(res == true) {
                mensaje("SE HA AGREGADO LA EVIDENCIA CORRECTAMENTE")
                imageViewe.setImageResource(R.drawable.image)
            } else {
                mensaje("HUBO UN ERROR AL AGREGAR LA EVIDENCIA")
            }
        }

    }
    private fun convierteBytes(bitmap : Bitmap):ByteArray{
        var stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,5,stream)
        return stream.toByteArray()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when{
            requestCode==SELECT_ACTIVITY && resultCode == Activity.RESULT_OK -> {
                imageUri=data!!.data
                imageViewe.setImageURI(imageUri)
            }
        }
    }
    private fun mensaje(s:String){
        AlertDialog.Builder(this)
            .setMessage(s)
            .setTitle("ATENCION")
            .setPositiveButton("OK"){d,i->}
            .show()
    }
}