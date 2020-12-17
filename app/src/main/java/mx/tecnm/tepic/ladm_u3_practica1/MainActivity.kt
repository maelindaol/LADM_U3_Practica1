package mx.tecnm.tepic.ladm_u3_practica1

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var baseDatos = BaseDatos(this, "basedatos1", null, 1)
    var listaID = ArrayList<String>()
    var idSeleccionadoLista = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.setTitle("Actividades");
        cargarContactos()
        buttonInsertar.setOnClickListener {
            insertar();
        }
        buttonBuscar.setOnClickListener {
            buscar();
        }

    }
    private fun buscar(){
        try {
            var tran = baseDatos.readableDatabase
            var actividades = ArrayList<String>()
            var resultados = tran.query("ACTIVIDADES",arrayOf("ID_ACTIVIDAD","DESCRIPCION","FECHA_CAPTURA","FECHA_ENTREGA"),"FECHA_ENTREGA=?",
                arrayOf(fechaent.text.toString()),null,null,null)
            listaID.clear()
            if (resultados.moveToFirst()){
                do {
                    var concatenacion =
                        "ID ACTIVIDAD:${resultados.getString(0)}\nDESCRIPCION:${resultados.getString(1)}\nFECHA CAPTURA:" +
                                "${resultados.getString(2)}\nFECHA ENTREGA: ${resultados.getString(3)}"
                    actividades.add(concatenacion)
                    listaID.add(resultados.getInt(0).toString())
                } while (resultados.moveToNext())
            }else{
                mensaje("NO SE ENCONTRARON RESULTADOS")
            }
            lista.adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1, actividades
            )
            this.registerForContextMenu(lista)
            lista.setOnItemClickListener { adapterView, view, i, l ->
                idSeleccionadoLista = i
                Toast.makeText(this, "Se seleccionó elemento", Toast.LENGTH_LONG)
                    .show()
            }
            tran.close()
        }catch (e:SQLiteException){
            mensaje(e.message!!)
        }
    }
    private fun insertar() {
        try {
            var trans = baseDatos.writableDatabase
            var variables = ContentValues()
            variables.put("DESCRIPCION", descripcion.text.toString())
            variables.put("FECHA_CAPTURA",fechacap.text.toString())
            variables.put("FECHA_ENTREGA",fechaent.text.toString())
            var respuesta = trans.insert("ACTIVIDADES", null, variables)
            if (respuesta == -1L) {
                mensaje("ERROR NO SE PUDO INSERTAR")
            } else {
                mensaje("SE INSERTÓ CON EXITO")
                limpiarCampos()
            }

            trans.close()
        } catch (e: SQLiteException) {
            mensaje(e.message!!)
        }
        cargarContactos()
    }

    private fun limpiarCampos() {
        descripcion.setText("")
        fechacap.setText("")
        fechaent.setText("")
    }

    private fun mensaje(s: String) {
        AlertDialog.Builder(this)
            .setMessage(s)
            .setTitle("ATENCION")
            .setPositiveButton("OK") { d, i -> }
            .show()
    }

    private fun cargarContactos() {
        try {
            var trans = baseDatos.readableDatabase
            var eventos = ArrayList<String>()
            var respuesta = trans.query("ACTIVIDADES", arrayOf("*"), null, null, null, null, null)
            listaID.clear()
            if (respuesta.moveToFirst()) {
                do {
                    var concatenacion =
                        "ID ACTIVIDAD:${respuesta.getString(0)}\nDESCRIPCION:${respuesta.getString(1)}\nFECHA CAPTURA:" +
                                "${respuesta.getString(2)}\nFECHA ENTREGA: ${respuesta.getString(3)}"
                    eventos.add(concatenacion)
                    listaID.add(respuesta.getInt(0).toString())
                } while (respuesta.moveToNext())
            } else {
                eventos.add("NO HAY ACTIVIDADES INSERTADAS")
            }
            lista.adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1, eventos
            )
            this.registerForContextMenu(lista)
            lista.setOnItemClickListener { adapterView, view, i, l ->
                idSeleccionadoLista = i
                Toast.makeText(this, "Se seleccionó elemento", Toast.LENGTH_LONG)
                    .show()
            }
            trans.close()
        } catch (e: SQLiteException) {
            mensaje(e.message!!)
        }
    }
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        var inflaterDB = menuInflater
        inflaterDB.inflate(R.menu.menuprincipal, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (idSeleccionadoLista == -1) {
            mensaje("ERROR! debes dar clic primero en un item para VISUALIZAR EVIDENCIAS O ACTUALIZAR/ELIMINAR ACTIVIDAD")
            return true
        }
        when (item.itemId) {
            R.id.itemactualizareliminar-> {
                var idEliminar = listaID.get(idSeleccionadoLista)
                var intent = Intent(this, MainActivity2::class.java)
                intent.putExtra("idactividad", idEliminar)
                startActivity(intent)
            }
            R.id.itemevidencias-> {
                var idEliminar = listaID.get(idSeleccionadoLista)
                var intent = Intent(this, MainActivity3::class.java)
                intent.putExtra("idactividad3", idEliminar)
                startActivity(intent)
            }
            R.id.itemsalir -> {
            }
        }
        idSeleccionadoLista = -1
        return true
    }
    override fun onResume() {
        super.onResume()
        cargarContactos()
    }

}