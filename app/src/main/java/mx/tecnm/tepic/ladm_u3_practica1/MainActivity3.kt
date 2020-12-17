package mx.tecnm.tepic.ladm_u3_practica1

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_evidencia.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main2.Actdescrip
import kotlinx.android.synthetic.main.activity_main2.Actfecha_cap
import kotlinx.android.synthetic.main.activity_main2.Actfecha_ent
import kotlinx.android.synthetic.main.activity_main2.idact
import kotlinx.android.synthetic.main.activity_main3.*
import kotlinx.android.synthetic.main.activity_main3.imageViewe
import java.io.ByteArrayOutputStream

class MainActivity3 : AppCompatActivity() {
    var baseDatos = BaseDatos(this, "basedatos1", null, 1)
    var listaID = ArrayList<String>()
    var idSeleccionadoLista = -1
    var id = "al"
    var foto:ByteArray? = ByteArray(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        this.setTitle("Evidencia");
        imageViewe.setImageResource(R.drawable.image)
        cargarEvidencias()
        var extra = intent.extras
        var id = extra!!.getString("idactividad3")!!
        idact3.setText(idact3.text.toString() + "${id}")
        try {
            var base = baseDatos.readableDatabase
            var respuesta = base.query(
                "ACTIVIDADES",
                arrayOf("DESCRIPCION", "FECHA_CAPTURA", "FECHA_ENTREGA"),
                "ID_ACTIVIDAD=?",
                arrayOf(id),
                null,
                null,
                null
            )
            if (respuesta.moveToFirst()) {
                Actdescrip3.setText(respuesta.getString(0))
                Actfecha_cap3.setText(respuesta.getString(1))
                Actfecha_ent3.setText(respuesta.getString(2))
            } else {
                mensaje("ERROR! no se encontró ID")
            }
            base.close()
        } catch (e: SQLiteException) {
            mensaje(e.message!!)
        }
        buttonInsertarEv.setOnClickListener {
            var intent = Intent(this, evidencia::class.java)
            intent.putExtra("idactividad", id)
            startActivity(intent)
        }
        buttonRegresar3.setOnClickListener {
            finish()
        }

    }

    private fun cargarEvidencias() {
        try {
            var trans = baseDatos.readableDatabase
            var eventos = ArrayList<String>()
            var respuesta = trans.query("EVIDENCIAS", arrayOf("*"), null, null, null, null, null)
            listaID.clear()
            if (respuesta.moveToFirst()) {
                do {
                    var concatenacion =
                        "ID EVIDENCIA:${respuesta.getString(0)}"
                    eventos.add(concatenacion)
                    listaID.add(respuesta.getInt(0).toString())
                } while (respuesta.moveToNext())
            } else {
                eventos.add("NO HAY EVIDENCIAS INSERTADAS")
            }
            Liste.adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1, eventos
            )
            this.registerForContextMenu(Liste)
            Liste.setOnItemClickListener { adapterView, view, i, l ->
                idSeleccionadoLista = i
                Toast.makeText(this, "Se seleccionó elemento", Toast.LENGTH_LONG)
                    .show()
                var img = evidencia2("",ByteArray(0))
                img.asignarPuntero(this)
                foto = img.buscaFoto(listaID.get(i))
                imageViewe.setImageBitmap(convertir(foto))
            }
            trans.close()
        } catch (e: SQLiteException) {
            mensaje(e.message!!)
        }
    }

    private fun convertir(imagen: ByteArray?): Bitmap? {
        return BitmapFactory.decodeByteArray(imagen, 0, imagen!!.size)
    }
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        var inflaterDB = menuInflater
        inflaterDB.inflate(R.menu.menusecundario, menu)
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (idSeleccionadoLista == -1) {
            mensaje("ERROR! debes dar clic primero en un item para ELIMINAR EVIDENCIA")
            return true
        }
        when (item.itemId) {
            R.id.itemelime-> {
                var idEliminar = listaID.get(idSeleccionadoLista)
                eliminar(idEliminar)
            }
            R.id.itemsalir -> {
            }
        }
        idSeleccionadoLista = -1
        return true
    }
    private fun eliminar(ideliminar: String) {
        try {
            var trans = baseDatos.writableDatabase
            var resultado = trans.delete(
                "EVIDENCIAS", "ID_EVIDENCIA=?",
                arrayOf(ideliminar)
            )
            if (resultado == 0) {
                mensaje("ERROR! No se pudo eliminar")
            } else {
                cargarEvidencias()
                mensaje("Se logro eliminar con éxito el ID ${ideliminar}")
            }
            trans.close()
        } catch (e: SQLiteException) {
            mensaje(e.message!!)
        }
    }
    private fun mensaje(s:String){
        AlertDialog.Builder(this)
            .setMessage(s)
            .setTitle("ATENCION")
            .setPositiveButton("OK"){d,i->}
            .show()
    }
    override fun onResume() {
        super.onResume()
        cargarEvidencias()
    }

}