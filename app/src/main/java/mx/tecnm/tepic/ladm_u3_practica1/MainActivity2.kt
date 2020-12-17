package mx.tecnm.tepic.ladm_u3_practica1

import android.content.ContentValues
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    var baseDatos = BaseDatos(this,"basedatos1",null,1)
    var id = "al"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        this.setTitle("Actualizar/Eliminar Actividades");
        var extra = intent.extras
        var id = extra!!.getString("idactividad")!!
        idact.setText(idact.text.toString()+"${id}")
        try {
           var base = baseDatos.readableDatabase
           var respuesta = base.query("ACTIVIDADES", arrayOf("DESCRIPCION","FECHA_CAPTURA","FECHA_ENTREGA"),"ID_ACTIVIDAD=?",
               arrayOf(id), null, null, null)
            if(respuesta.moveToFirst()){
                Actdescrip.setText(respuesta.getString(0))
                Actfecha_cap.setText(respuesta.getString(1))
                Actfecha_ent.setText(respuesta.getString(2))
            }else{
              mensaje("ERROR! no se encontró ID")
            }
            base.close()
        }catch (e: SQLiteException){
            mensaje(e.message!!)
        }
        buttonEliminarActividad.setOnClickListener {
          eliminar(id)
          finish()
        }
        buttonActAct.setOnClickListener {
            actualizar(id)
        }
        buttonRegresar.setOnClickListener {
            finish()
        }
    }
    private fun eliminar(ideliminar: String) {
        try {
            var trans = baseDatos.writableDatabase
            var resultado = trans.delete(
                "ACTIVIDADES", "ID_ACTIVIDAD=?",
                arrayOf(ideliminar)
            )
            if (resultado == 0) {
                mensaje("ERROR! No se pudo eliminar")
            } else {
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
    private fun actualizar(id:String){
        try {
            var trans = baseDatos.writableDatabase
            var valores = ContentValues()
            valores.put("DESCRIPCION",Actdescrip.text.toString())
            valores.put("FECHA_CAPTURA",Actfecha_cap.text.toString())
            valores.put("FECHA_ENTREGA",Actfecha_ent.text.toString())
            var res = trans.update("ACTIVIDADES",valores,"ID_ACTIVIDAD=?", arrayOf(id))
            if (res>0){
                mensaje("Se actualizó correctamente ID ${id}")
                finish()
            }else{
                mensaje("No se pudo actualizar ID")
            }
            trans.close()
        }catch (e:SQLiteException){
            mensaje(e.message!!)
        }

    }
}