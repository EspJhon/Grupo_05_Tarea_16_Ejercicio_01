package com.example.grupo_05_tarea_16_ejercicio_01.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.grupo_05_tarea_16_ejercicio_01.modelo.PuestoControl;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;

import java.util.ArrayList;

public class DBAdapter {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DB_Grupo_05_Tarea_16_Ejercicio_01";

    //tabla propietario
    private static final class Table_Propietario {
        public static final String TABLE = "table_propietario";
        public static final String ID = "IdPropietario";
        public static final String CEDULA = "cedulap";
        public static final String NOMBRE = "nombre";
        public static final String CIUDAD = "ciudad";
    }

    private static final class Table_Vehiculo {
        public static final String TABLE = "table_Vehiculo";
        public static final String ID = "IdVehiculo";
        public static final String NUMERO_PLACA = "numplaca";
        public static final String MARCA = "marca";
        public static final String MODELO = "modelo";
        public static final String MOTOR = "motor";
        public static final String FECHA_ANO = "f_ano";
    }

    private static final class Table_Oficina {
        public static final String TABLE = "table_oficina_gob";
        public static final String ID = "IdOficina";
        public static final String VALOR_VEHICULO = "valor_vehiculo";
        public static final String NUMERO_POLIZA = "npoliza";
        public static final String UBICACION = "ubicacion";
    }

    private static final class Table_Accidente {
        public static final String TABLE = "table_accidente";
        public static final String ID = "Idaccidente";
        public static final String HORA = "hora";
        public static final String FECHA = "fecha";
        public static final String DESCRIPCION = "descripcion";
    }

    private static final class Table_Infraccion {
        public static final String TABLE = "table_infraccion";
        public static final String ID = "IdInfraccion";
        public static final String VALOR_MULTA = "valormulta";
        public static final String FECHA = "fecha";
        public static final String HORA = "hora";
    }

    private static final class Table_Norma {
        public static final String TABLE = "table_norma";
        public static final String ID = "Idnomra";
        public static final String NUMERO_NORMA = "numnorma";
        public static final String DESCRIPCION = "descripcion";
    }

    private static final class Table_Audiencia {
        public static final String TABLE = "table_audiencia";
        public static final String ID = "IdAudiencia";
        public static final String CODIGO = "codigo";
        public static final String LUGAR = "lugar";
        public static final String FECHA = "fecha";
        public static final String HORA = "hora";
    }

    private static final class Table_Acta {
        public static final String TABLE = "table_acta";
        public static final String ID = "IdActa";
        public static final String CODIGO = "codigo";
        public static final String HORA = "hora";
        public static final String FECHA = "fecha";
    }

    private static final class Table_Agente {
        public static final String TABLE = "table_agente";
        public static final String ID = "Idagente";
        public static final String CEDULA = "cedulaa";
        public static final String NOMBRE = "Nombre";
        public static final String RANGO = "rango";
    }

    private static final class Table_Zona {
        public static final String TABLE = "table_zona";
        public static final String ID = "IdZona";
        public static final String DEPARTAMENTO = "departamento";
        public static final String PROVINCIA = "provincia";
        public static final String DISTRITO = "distrito";
        public static final String LATITUD = "latitud";
        public static final String LONGITUD = "longitud";
        public static final String TITULO = "titulo";
    }

    private static final class Table_Puesto_Control {
        public static final String TABLE = "table_puesto_control";
        public static final String ID = "IdPuestoControl";
        public static final String REFERENCIA = "referencia";
        public static final String LATITUD = "latitud";
        public static final String LONGITUD = "longitud";
        public static final String TITULO = "titulo";
    }

    private static final String CREATE_PROPIETARIO =
            "create table " + Table_Propietario.TABLE + " (" +
                    Table_Propietario.ID + " integer primary key autoincrement, " +
                    Table_Propietario.CEDULA + " integer not null, " +
                    Table_Propietario.NOMBRE + " text not null, " +
                    Table_Propietario.CIUDAD + " text not null );";
    private static final String CREATE_VEHICULO =
            "create table " + Table_Vehiculo.TABLE + " (" +
                    Table_Vehiculo.ID + " integer primary key autoincrement, " +
                    Table_Vehiculo.NUMERO_PLACA + " integer not null, " +
                    Table_Vehiculo.MARCA + " text not null, " +
                    Table_Vehiculo.MODELO + " text not null, " +
                    Table_Vehiculo.MOTOR + " text not null, " +
                    Table_Vehiculo.FECHA_ANO + " text not null, " +
                    Table_Propietario.ID + " integer not null, " +
                    "FOREIGN KEY (" + Table_Propietario.ID + ") REFERENCES " + Table_Propietario.TABLE + "(" + Table_Propietario.ID + ") );";
    private static final String CREATE_OFICINAGOB =
            "create table " + Table_Oficina.TABLE + " (" +
                    Table_Oficina.ID + " integer primary key autoincrement, " +
                    Table_Oficina.VALOR_VEHICULO + " text not null, " +
                    Table_Oficina.NUMERO_POLIZA + " integer not null, " +
                    Table_Vehiculo.ID + " integer not null, " +
                    Table_Oficina.UBICACION + " text not null, " +
                    "FOREIGN KEY (" + Table_Vehiculo.ID + ") REFERENCES " + Table_Vehiculo.TABLE + "(" + Table_Vehiculo.ID + ") );";
    private static final String CREATE_ACCIDENTE =
            "create table " + Table_Accidente.TABLE + " (" +
                    Table_Accidente.ID + " integer primary key autoincrement, " +
                    Table_Vehiculo.ID + " integer not null, " +
                    Table_Agente.ID + " integer not null, " +
                    Table_Accidente.HORA + " text not null, " +
                    Table_Accidente.FECHA + " text not null, " +
                    Table_Accidente.DESCRIPCION + " text not null, " +
                    "FOREIGN KEY (" + Table_Vehiculo.ID + ") REFERENCES " + Table_Vehiculo.TABLE + "(" + Table_Vehiculo.ID + "), " +
                    "FOREIGN KEY (" + Table_Agente.ID + ") REFERENCES " + Table_Agente.TABLE + "(" + Table_Agente.ID + ") );";
    private static final String CREATE_INFRACCION =
            "create table " + Table_Infraccion.TABLE + " (" +
                    Table_Infraccion.ID + " integer primary key autoincrement, " +
                    Table_Agente.ID + " integer not null, " +
                    Table_Vehiculo.ID + " integer not null, " +
                    Table_Infraccion.VALOR_MULTA + " text not null, " +
                    Table_Infraccion.FECHA + " text not null, " +
                    Table_Norma.ID + " integer not null, " +
                    Table_Infraccion.HORA + " text not null, " +
                    "FOREIGN KEY (" + Table_Vehiculo.ID + ") REFERENCES " + Table_Vehiculo.TABLE + "(" + Table_Vehiculo.ID + "), " +
                    "FOREIGN KEY (" + Table_Norma.ID + ") REFERENCES " + Table_Norma.TABLE + "(" + Table_Norma.ID + "), " +
                    "FOREIGN KEY (" + Table_Agente.ID + ") REFERENCES " + Table_Agente.TABLE + "(" + Table_Agente.ID + ") );";
    private static final String CREATE_NORMASDET =
            "create table " + Table_Norma.TABLE + " (" +
                    Table_Norma.ID + " integer primary key autoincrement, " +
                    Table_Norma.NUMERO_NORMA + " integer not null, " +
                    Table_Norma.DESCRIPCION + " text not null );";
    private static final String CREATE_AUDIENCIA =
            "create table " + Table_Audiencia.TABLE + " (" +
                    Table_Audiencia.ID + " integer primary key autoincrement, " +
                    Table_Audiencia.CODIGO + " integer not null, " +
                    Table_Audiencia.LUGAR + " text not null, " +
                    Table_Audiencia.FECHA + " text not null, " +
                    Table_Audiencia.HORA + " text not null );";
    private static final String CREATE_ZONA =
            "create table " + Table_Zona.TABLE + " (" +
                    Table_Zona.ID + " integer primary key autoincrement, " +
                    Table_Zona.DEPARTAMENTO + " text not null, " +
                    Table_Zona.PROVINCIA + " text not null, " +
                    Table_Zona.DISTRITO + " text not null, " +
                    Table_Zona.LATITUD + " text not null, " +
                    Table_Zona.LONGITUD + " text not null, " +
                    Table_Zona.TITULO + " text not null );";
    private static final String CREATE_ACTA =
            "create table " + Table_Acta.TABLE + " (" +
                    Table_Acta.ID + " integer primary key autoincrement, " +
                    Table_Accidente.ID + " integer not null, " +
                    Table_Audiencia.ID + " integer not null, " +
                    Table_Acta.HORA + " text not null, " +
                    Table_Zona.ID + " integer not null, " +
                    Table_Agente.ID + " integer not null, " +
                    Table_Acta.FECHA + " text not null, " +
                    "FOREIGN KEY (" + Table_Accidente.ID + ") REFERENCES " + Table_Accidente.TABLE + "(" + Table_Accidente.ID + "), " +
                    "FOREIGN KEY (" + Table_Audiencia.ID + ") REFERENCES " + Table_Audiencia.TABLE + "(" + Table_Audiencia.ID + "), " +
                    "FOREIGN KEY (" + Table_Zona.ID + ") REFERENCES " + Table_Zona.TABLE + "(" + Table_Zona.ID + "), " +
                    "FOREIGN KEY (" + Table_Agente.ID + ") REFERENCES " + Table_Agente.TABLE + "(" + Table_Agente.ID + ") );";
    private static final String CREATE_PUESTO_CONTROL =
            "create table " + Table_Puesto_Control.TABLE + " (" +
                    Table_Puesto_Control.ID + " integer primary key autoincrement, " +
                    Table_Zona.ID + " integer not null, " +
                    Table_Puesto_Control.REFERENCIA + " text not null, " +
                    Table_Puesto_Control.LATITUD + " text not null, " +
                    Table_Puesto_Control.LONGITUD + " text not null, " +
                    Table_Puesto_Control.TITULO + " text not null, " +
                    "FOREIGN KEY (" + Table_Zona.ID + ") REFERENCES " + Table_Zona.TABLE + "(" + Table_Zona.ID + ") );";
    private static final String CREATE_AGENTE =
            "create table " + Table_Agente.TABLE + " (" +
                    Table_Agente.ID + " integer primary key autoincrement, " +
                    Table_Agente.CEDULA + " integer not null, " +
                    Table_Agente.NOMBRE + " text not null, " +
                    Table_Puesto_Control.ID + " integer not null, " +
                    Table_Agente.RANGO + " text not null, " +
                    "FOREIGN KEY (" + Table_Puesto_Control.ID + ") REFERENCES " + Table_Puesto_Control.TABLE + "(" + Table_Puesto_Control.ID + ") );";

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private static Context context;

    public DBAdapter(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_PROPIETARIO);
            db.execSQL(CREATE_VEHICULO);
            db.execSQL(CREATE_OFICINAGOB);
            db.execSQL(CREATE_ACCIDENTE);
            db.execSQL(CREATE_INFRACCION);
            db.execSQL(CREATE_NORMASDET);
            db.execSQL(CREATE_AUDIENCIA);
            db.execSQL(CREATE_ACTA);
            db.execSQL(CREATE_AGENTE);
            db.execSQL(CREATE_ZONA);
            db.execSQL(CREATE_PUESTO_CONTROL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + Table_Propietario.TABLE);
            db.execSQL(CREATE_PROPIETARIO);
            db.execSQL("drop table if exists " + Table_Vehiculo.TABLE);
            db.execSQL(CREATE_VEHICULO);
            db.execSQL("drop table if exists " + Table_Oficina.TABLE);
            db.execSQL(CREATE_OFICINAGOB);
            db.execSQL("drop table if exists " + Table_Accidente.TABLE);
            db.execSQL(CREATE_ACCIDENTE);
            db.execSQL("drop table if exists " + Table_Infraccion.TABLE);
            db.execSQL(CREATE_INFRACCION);
            db.execSQL("drop table if exists " + Table_Norma.TABLE);
            db.execSQL(CREATE_NORMASDET);
            db.execSQL("drop table if exists " + Table_Audiencia.TABLE);
            db.execSQL(CREATE_AUDIENCIA);
            db.execSQL("drop table if exists " + Table_Acta.TABLE);
            db.execSQL(CREATE_ACTA);
            db.execSQL("drop table if exists " + Table_Agente.TABLE);
            db.execSQL(CREATE_AGENTE);
            db.execSQL("drop table if exists " + Table_Zona.TABLE);
            db.execSQL(CREATE_ZONA);
            db.execSQL("drop table if exists " + Table_Puesto_Control.TABLE);
            db.execSQL(CREATE_PUESTO_CONTROL);
        }
    }

    public DBAdapter open() throws SQLiteException {
        try {
            db = databaseHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            Toast.makeText(context, "Error al abrir Base de datos", Toast.LENGTH_SHORT).show();
        }
        return this;
    }

    public boolean isOpen() {
        if (db == null) {
            return false;
        }
        return db.isOpen();
    }

    public void close() {
        databaseHelper.close();
        db.close();
    }

    public void Insertar_Zona(Zona zona) {
        ContentValues values = new ContentValues();
        values.put(Table_Zona.DEPARTAMENTO, zona.getDepartamento());
        values.put(Table_Zona.PROVINCIA, zona.getProvincia());
        values.put(Table_Zona.DISTRITO, zona.getDistrito());
        values.put(Table_Zona.LATITUD, zona.getLatitud());
        values.put(Table_Zona.LONGITUD, zona.getLongitud());
        values.put(Table_Zona.TITULO, zona.getTitulo());
        db.insert(Table_Zona.TABLE, null, values);
    }
    public ArrayList<Zona> get_all_Zonas(){
        ArrayList<Zona> zonas = new ArrayList<>();
        try {
            String query = "select * from " + Table_Zona.TABLE;
            Cursor cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()){
                do {
                    Zona zona = new Zona();
                    zona.setIdZona(cursor.getInt(0));
                    zona.setDepartamento(cursor.getString(1));
                    zona.setProvincia(cursor.getString(2));
                    zona.setDistrito(cursor.getString(3));
                    zona.setLatitud(cursor.getString(4));
                    zona.setLongitud(cursor.getString(5));
                    zona.setTitulo(cursor.getString(6));
                    zonas.add(zona);
                }while (cursor.moveToNext());
            }
        } catch (Exception ex){
            return null;
        }
        return zonas;
    }
    public Zona get_Zona(String latitud, String longitud) {
        try {
            String query = "SELECT * FROM " + Table_Zona.TABLE +
                    " WHERE " + Table_Zona.LATITUD + " = ? AND " + Table_Zona.LONGITUD + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{latitud, longitud});
            Zona zona = null;
            if (cursor.moveToFirst()){
                do {
                    zona = new Zona();
                    zona.setIdZona(cursor.getInt(0));
                    zona.setDepartamento(cursor.getString(1));
                    zona.setProvincia(cursor.getString(2));
                    zona.setDistrito(cursor.getString(3));
                    zona.setLatitud(cursor.getString(4));
                    zona.setLongitud(cursor.getString(5));
                    zona.setTitulo(cursor.getString(6));
                }while (cursor.moveToNext());
            }
            cursor.close();
            return zona;
        } catch (Exception ex) {
            return null;
        }
    }
    public void Actualizar_Zona(Zona zona) {
        ContentValues values = new ContentValues();
        values.put(Table_Zona.DEPARTAMENTO, zona.getDepartamento());
        values.put(Table_Zona.PROVINCIA, zona.getProvincia());
        values.put(Table_Zona.DISTRITO, zona.getDistrito());
        values.put(Table_Zona.LATITUD, zona.getLatitud());
        values.put(Table_Zona.LONGITUD, zona.getLongitud());
        values.put(Table_Zona.TITULO, zona.getTitulo());
        db.update(Table_Zona.TABLE, values,Table_Zona.ID + " = " + zona.getIdZona(),null);
    }
    public void Eliminar_Zona(Zona zona) {
        ContentValues values = new ContentValues();
        db.delete(Table_Zona.TABLE,Table_Zona.ID + " = " + zona.getIdZona(),null);
    }

    public void Insertar_Puesto_Control(PuestoControl puestoControl) {
        ContentValues values = new ContentValues();
        values.put(Table_Zona.ID, puestoControl.getIdZona());
        values.put(Table_Puesto_Control.REFERENCIA, puestoControl.getReferencia());
        values.put(Table_Puesto_Control.LATITUD, puestoControl.getLatitud());
        values.put(Table_Puesto_Control.LONGITUD, puestoControl.getLongitud());
        values.put(Table_Puesto_Control.TITULO, puestoControl.getTitulo());
        db.insert(Table_Puesto_Control.TABLE, null, values);
    }
    public ArrayList<PuestoControl> get_all_Puesto_Controls(){
        ArrayList<PuestoControl> puestoControls = new ArrayList<>();
        try {
            String query = "select * from " + Table_Puesto_Control.TABLE;
            Cursor cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()){
                do {
                    PuestoControl puestoControl = new PuestoControl();
                    puestoControl.setIdPuestoControl(cursor.getInt(0));
                    puestoControl.setIdZona(cursor.getInt(1));
                    puestoControl.setReferencia(cursor.getString(2));
                    puestoControl.setLatitud(cursor.getString(3));
                    puestoControl.setLongitud(cursor.getString(4));
                    puestoControl.setTitulo(cursor.getString(5));
                    puestoControls.add(puestoControl);
                }while (cursor.moveToNext());
            }
        } catch (Exception ex){
            return null;
        }
        return puestoControls;
    }
}
