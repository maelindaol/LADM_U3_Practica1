package mx.tecnm.tepic.ladm_u3_practica1

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos (
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE ACTIVIDADES(ID_ACTIVIDAD INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,DESCRIPCION VARCHAR(2000),FECHA_CAPTURA DATE,FECHA_ENTREGA DATE)")
            db.execSQL("CREATE TABLE EVIDENCIAS(ID_EVIDENCIA INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,ID_ACTIVIDAD INTEGER REFERENCES ACTIVIDADES(ID_ACTIVIDAD),FOTO BLOB)")
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

        }
}