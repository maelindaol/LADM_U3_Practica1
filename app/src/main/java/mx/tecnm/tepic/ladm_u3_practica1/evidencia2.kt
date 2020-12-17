package mx.tecnm.tepic.ladm_u3_practica1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import java.util.ArrayList

class evidencia2(i:String, f:ByteArray?) {
    var id_act = i
    var foto = f
    var puntero : Context?= null
    fun asignarPuntero(p: Context) {
        puntero = p
    }

    fun insertar():Boolean {
        try {
            var base = BaseDatos(puntero!!, "basedatos1", null, 1)
            var trans = base.writableDatabase
            var variables = ContentValues()
            variables.put("ID_ACTIVIDAD", id_act.toInt())
            variables.put("FOTO", foto)
            var respuesta = trans.insert("EVIDENCIAS", null,variables)
            if (respuesta.toInt() == -1) {
                return false
            }
        }catch (e: SQLiteException) {
            return false
        }
        return true
    }


    fun buscaFoto(id:String): ByteArray?{
        var registro = evidencia2("",ByteArray(0))
        try {
            var bd = BaseDatos(puntero!!, "basedatos1", null, 1)
            var select = bd.readableDatabase
            var busca = arrayOf("FOTO")
            var buscaID = arrayOf(id)
            var res = select.query("EVIDENCIAS", busca, "ID_EVIDENCIA = ?",buscaID, null, null, null)
            if(res.moveToFirst()){
                registro.foto = res.getBlob(0)
            }
        }catch (e: SQLiteException){
            e.message.toString()
        }
        return registro.foto
    }


}