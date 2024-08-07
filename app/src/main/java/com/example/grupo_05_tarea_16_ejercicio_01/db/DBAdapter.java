package com.example.grupo_05_tarea_16_ejercicio_01.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Acta;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Agente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Audiencia;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Infraccion;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.NormasDet;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.OficinaGob;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Propietario;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.PuestoControl;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Usuario;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Vehiculo;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;

import java.util.ArrayList;

public class DBAdapter {

    private static final int DATABASE_VERSION = 5;
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
        public static final String LATITUD = "latitud";
        public static final String LONGITUD = "longitud";
    }

    private static final class Table_Accidente {
        public static final String TABLE = "table_accidente";
        public static final String ID = "Idaccidente";
        public static final String HORA = "hora";
        public static final String FECHA = "fecha";

        public static final String TITULO = "titulo";
        public static final String DESCRIPCION = "descripcion";
        public static final String URL = "URLimagen";
        public static final String NOMBRE_LUGAR = "nombreLugar";
        public static final String LATITUD = "latitud";
        public static final String LONGITUD = "longitud";
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

    private static final class Table_Usuario {
        public static final String TABLE = "table_usuario";
        public static final String ID = "IdUsuario";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
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
                    Table_Oficina.LATITUD + " text not null, " +
                    Table_Oficina.LONGITUD + " text not null, " +
                    "FOREIGN KEY (" + Table_Vehiculo.ID + ") REFERENCES " + Table_Vehiculo.TABLE + "(" + Table_Vehiculo.ID + ") );";
    private static final String CREATE_ACCIDENTE =
            "create table " + Table_Accidente.TABLE + " (" +
                    Table_Accidente.ID + " integer primary key autoincrement, " +
                    Table_Vehiculo.ID + " integer not null, " +
                    Table_Agente.ID + " integer not null, " +
                    Table_Accidente.HORA + " text not null, " +
                    Table_Accidente.FECHA + " text not null, " +
                    Table_Accidente.TITULO + " text not null, " +
                    Table_Accidente.DESCRIPCION + " text not null, " +
                    Table_Accidente.URL + " text not null, " +
                    Table_Accidente.NOMBRE_LUGAR + " text not null, " +
                    Table_Accidente.LATITUD + " text not null, " +
                    Table_Accidente.LONGITUD + " text not null, " +
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
                    Table_Norma.NUMERO_NORMA + " text not null, " +
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
                    Table_Acta.CODIGO + " integer not null, " +
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

    private static final String CREATE_USUARIO =
            "create table " + Table_Usuario.TABLE + " (" +
                    Table_Usuario.ID + " integer primary key autoincrement, " +
                    Table_Usuario.USERNAME + " text not null unique, " +
                    Table_Usuario.PASSWORD + " text not null );";

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
            db.execSQL(CREATE_USUARIO);
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
            db.execSQL("drop table if exists " + Table_Usuario.TABLE);
            db.execSQL(CREATE_USUARIO);
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


    //MÉTODOS TABLA ACCIDENTE
    public void Insertar_Accidente(Accidente accidente) {
        ContentValues values = new ContentValues();
        values.put(Table_Vehiculo.ID, accidente.getIdVehiculo());
        values.put(Table_Agente.ID, accidente.getIdagente());
        values.put(Table_Accidente.HORA, accidente.getHora());
        values.put(Table_Accidente.FECHA, accidente.getFecha());
        values.put(Table_Accidente.TITULO, accidente.getTitulo());
        values.put(Table_Accidente.DESCRIPCION, accidente.getDescripcion());
        values.put(Table_Accidente.URL, accidente.getURLimagen());
        values.put(Table_Accidente.NOMBRE_LUGAR, accidente.getNombreLugar());
        values.put(Table_Accidente.LATITUD, accidente.getLatitud());
        values.put(Table_Accidente.LONGITUD, accidente.getLongitud());
        db.insert(Table_Accidente.TABLE, null, values);
    }

    public void Actualizar_Accidente(Accidente accidente) {
        ContentValues values = new ContentValues();
        values.put(Table_Vehiculo.ID, accidente.getIdVehiculo());
        values.put(Table_Agente.ID, accidente.getIdagente());
        values.put(Table_Accidente.HORA, accidente.getHora());
        values.put(Table_Accidente.FECHA, accidente.getFecha());
        values.put(Table_Accidente.TITULO, accidente.getTitulo());
        values.put(Table_Accidente.DESCRIPCION, accidente.getDescripcion());
        values.put(Table_Accidente.URL, accidente.getURLimagen());
        values.put(Table_Accidente.NOMBRE_LUGAR, accidente.getNombreLugar());
        values.put(Table_Accidente.LATITUD, accidente.getLatitud());
        values.put(Table_Accidente.LONGITUD, accidente.getLongitud());
        db.update(Table_Accidente.TABLE, values, Table_Accidente.ID + " = " + accidente.getIdaccidente(), null);
    }

    public void Eliminar_Accidente(Accidente accidente) {
        db.delete(Table_Accidente.TABLE, Table_Accidente.ID + " = " + accidente.getIdaccidente(), null);
    }

    public Accidente get_Accidente(int id) {
        try {
            String query = "SELECT * FROM " + Table_Accidente.TABLE +
                    " WHERE " + Table_Accidente.ID + " = " + id;
            Cursor cursor = db.rawQuery(query, null);
            Accidente accidente = null;
            if (cursor.moveToFirst()) {
                do {
                    accidente = new Accidente();
                    accidente.setIdaccidente(cursor.getInt(0));
                    accidente.setIdVehiculo(cursor.getInt(1));
                    accidente.setIdagente(cursor.getInt(2));
                    accidente.setHora(cursor.getString(3));
                    accidente.setFecha(cursor.getString(4));
                    accidente.setTitulo(cursor.getString(5));
                    accidente.setDescripcion(cursor.getString(6));
                    accidente.setURLimagen(cursor.getString(7));
                    accidente.setNombreLugar(cursor.getString(8));
                    accidente.setLatitud(cursor.getString(9));
                    accidente.setLongitud(cursor.getString(10));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return accidente;
        } catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<Accidente> get_all_Accidente() {
        ArrayList<Accidente> accidentes = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + Table_Accidente.TABLE;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    Accidente accidente = new Accidente();
                    accidente.setIdaccidente(cursor.getInt(0));
                    accidente.setIdVehiculo(cursor.getInt(1));
                    accidente.setIdagente(cursor.getInt(2));
                    accidente.setHora(cursor.getString(3));
                    accidente.setFecha(cursor.getString(4));
                    accidente.setTitulo(cursor.getString(5));
                    accidente.setDescripcion(cursor.getString(6));
                    accidente.setURLimagen(cursor.getString(7));
                    accidente.setNombreLugar(cursor.getString(8));
                    accidente.setLatitud(cursor.getString(9));
                    accidente.setLongitud(cursor.getString(10));
                    accidentes.add(accidente);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception ex) {
            return null;
        }
        return accidentes;
    }


    //MÉTODOS TABLA AUDIENCIA

    private boolean isValidDate(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private boolean isValidTime(String time) {
        return time.matches("\\d{2}:\\d{2}:\\d{2}");
    }



    public void Insertar_Audiencia(Audiencia audiencia) {

        if (!isValidDate(audiencia.getFecha())) {
            throw new IllegalArgumentException("Formato de fecha incorrecto, debe ser YYYY-MM-DD");
        }
        if (!isValidTime(audiencia.getHora())) {
            throw new IllegalArgumentException("Formato de hora incorrecto, debe ser HH:MM:SS");
        }

        ContentValues values = new ContentValues();
        values.put(Table_Audiencia.CODIGO, audiencia.getCodigo());
        values.put(Table_Audiencia.LUGAR, audiencia.getLugar());
        values.put(Table_Audiencia.FECHA, audiencia.getFecha());
        values.put(Table_Audiencia.HORA, audiencia.getHora());
        db.insert(Table_Audiencia.TABLE, null, values);
    }

    public void Actualizar_Audiencia(Audiencia audiencia) {

        if (!isValidDate(audiencia.getFecha())) {
            throw new IllegalArgumentException("Formato de fecha incorrecto, debe ser YYYY-MM-DD");
        }
        if (!isValidTime(audiencia.getHora())) {
            throw new IllegalArgumentException("Formato de hora incorrecto, debe ser HH:MM:SS");
        }

        ContentValues values = new ContentValues();
        values.put(Table_Audiencia.CODIGO, audiencia.getCodigo());
        values.put(Table_Audiencia.LUGAR, audiencia.getLugar());
        values.put(Table_Audiencia.FECHA, audiencia.getFecha());
        values.put(Table_Audiencia.HORA, audiencia.getHora());
        db.update(Table_Audiencia.TABLE, values, Table_Audiencia.ID + " = " + audiencia.getIdAudiencia(), null);
    }

    public void Eliminar_Audiencia(Audiencia audiencia) {
        db.delete(Table_Audiencia.TABLE, Table_Audiencia.ID + " = " + audiencia.getIdAudiencia(), null);
    }

    public Audiencia get_Audiencia(int id) {
        try {
            String query = "SELECT * FROM " + Table_Audiencia.TABLE +
                    " WHERE " + Table_Audiencia.ID + " = " + id;
            Cursor cursor = db.rawQuery(query, null);
            Audiencia audiencia = null;
            if (cursor.moveToFirst()) {
                do {
                    audiencia = new Audiencia();
                    audiencia.setIdAudiencia(cursor.getInt(0));
                    audiencia.setCodigo(cursor.getInt(1));
                    audiencia.setLugar(cursor.getString(2));
                    audiencia.setFecha(cursor.getString(3));
                    audiencia.setHora(cursor.getString(4));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return audiencia;
        } catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<Audiencia> get_all_Audiencia() {
        ArrayList<Audiencia> audiencias = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + Table_Audiencia.TABLE;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    Audiencia audiencia = new Audiencia();
                    audiencia.setIdAudiencia(cursor.getInt(0));
                    audiencia.setCodigo(cursor.getInt(1));
                    audiencia.setLugar(cursor.getString(2));
                    audiencia.setFecha(cursor.getString(3));
                    audiencia.setHora(cursor.getString(4));
                    audiencias.add(audiencia);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception ex) {
            return null;
        }
        return audiencias;
    }


    //MÉTODOS DE INICIO DE SESIÓN

    public void insertarUsuario(Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put(Table_Usuario.USERNAME, usuario.getUsername());
        values.put(Table_Usuario.PASSWORD, usuario.getPassword());
        db.insert(Table_Usuario.TABLE, null, values);
    }

    public ArrayList<Usuario> get_all_Usuarios(){

        ArrayList<Usuario> usuarios = new ArrayList<>();
        try{
            String query = "select * from " + Table_Usuario.TABLE;
            Cursor cursor = db.rawQuery(query,null);
            if(cursor.moveToFirst()){
                do{
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(cursor.getInt(0));
                    usuario.setUsername(cursor.getString(1));
                    usuario.setPassword(cursor.getString(1));
                    usuarios.add(usuario);
                }while(cursor.moveToNext());
            }
        }catch (Exception ex){
            return null;
        }
        return usuarios;
    }

    public boolean validarUsuario(Usuario usuario) {
        Cursor cursor = db.query(Table_Usuario.TABLE,
                new String[]{Table_Usuario.ID},
                Table_Usuario.USERNAME + "=? AND " + Table_Usuario.PASSWORD + "=?",
                new String[]{usuario.getUsername(), usuario.getPassword()},
                null, null, null);
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }

    public int obtenerIdUsuario(Usuario usuario) {
        Cursor cursor = db.query(Table_Usuario.TABLE,
                new String[]{Table_Usuario.ID},
                Table_Usuario.USERNAME + "=? AND " + Table_Usuario.PASSWORD + "=?",
                new String[]{usuario.getUsername(), usuario.getPassword()},
                null, null, null);
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        return userId;
    }


    //PROPIETARIO

    public void insertarPropietario(Propietario propietario) {
        ContentValues values = new ContentValues();
        values.put(Table_Propietario.CEDULA, propietario.getCedulap());
        values.put(Table_Propietario.NOMBRE, propietario.getNombre());
        values.put(Table_Propietario.CIUDAD, propietario.getCiudad());
        db.insert(Table_Propietario.TABLE, null, values);
    }

    public void actualizarPropietario(Propietario propietario) {
        ContentValues values = new ContentValues();
        values.put(Table_Propietario.CEDULA, propietario.getCedulap());
        values.put(Table_Propietario.NOMBRE, propietario.getNombre());
        values.put(Table_Propietario.CIUDAD, propietario.getCiudad());
        db.update(Table_Propietario.TABLE,values,Table_Propietario.ID + " = " + propietario.getIdPropietario(),null);
    }


    public void Eliminar_Propietario (Propietario propietario){
        ContentValues values = new ContentValues();
        db.delete(Table_Propietario.TABLE,Table_Propietario.ID + " = " + propietario.getIdPropietario(),null);
    }

    public ArrayList<Propietario> get_all_Propietarios(){

        ArrayList<Propietario> propietarios = new ArrayList<>();
        try{
            String query = "select * from " + Table_Propietario.TABLE;
            Cursor cursor = db.rawQuery(query,null);
            if(cursor.moveToFirst()){
                do{
                    Propietario propietario = new Propietario();
                    propietario.setIdPropietario(cursor.getInt(0));
                    propietario.setCedulap(cursor.getInt(1));
                    propietario.setNombre(cursor.getString(2));
                    propietario.setCiudad(cursor.getString(3));
                    propietarios.add(propietario);
                }while(cursor.moveToNext());
            }
        }catch (Exception ex){
            return null;
        }
        return propietarios;
    }

    public Propietario get_Propietario(int id){

        ArrayList<Propietario> propietarios = new ArrayList<>();
        try{
            String query = "select * from " + Table_Propietario.TABLE +
                    " where " + Table_Propietario.ID + " = " + id;
            Cursor cursor = db.rawQuery(query,null);
            Propietario propietario = null;

            if(cursor.moveToFirst()){
                do{
                    propietario = new Propietario();
                    propietario.setIdPropietario(cursor.getInt(0));
                    propietario.setCedulap(cursor.getInt(1));
                    propietario.setNombre(cursor.getString(2));
                    propietario.setCiudad(cursor.getString(3));
                    propietarios.add(propietario);
                }while(cursor.moveToNext());
            }
            cursor.close();
            return propietario;

        }catch (Exception ex){
            return null;
        }

    }


    //VEHÍCULO

    public void Insertar_Vehiculo(Vehiculo vehiculo) {
        ContentValues values = new ContentValues();
        values.put(Table_Vehiculo.NUMERO_PLACA, vehiculo.getNumplaca());
        values.put(Table_Vehiculo.MARCA, vehiculo.getMarca());
        values.put(Table_Vehiculo.MODELO, vehiculo.getModelo());
        values.put(Table_Vehiculo.MOTOR, vehiculo.getMotor());
        values.put(Table_Vehiculo.FECHA_ANO, vehiculo.getF_ano());
        values.put(Table_Propietario.ID, vehiculo.getIdPropietario());

        db.insert(Table_Vehiculo.TABLE, null, values);
    }

    public void Actualizar_Vehiculo(Vehiculo vehiculo) {
        ContentValues values = new ContentValues();
        values.put(Table_Vehiculo.NUMERO_PLACA, vehiculo.getNumplaca());
        values.put(Table_Vehiculo.MARCA, vehiculo.getMarca());
        values.put(Table_Vehiculo.MODELO, vehiculo.getModelo());
        values.put(Table_Vehiculo.MOTOR, vehiculo.getMotor());
        values.put(Table_Vehiculo.FECHA_ANO, vehiculo.getF_ano());
        values.put(Table_Propietario.ID, vehiculo.getIdPropietario());
        db.update(Table_Vehiculo.TABLE, values, Table_Vehiculo.ID + " = " + vehiculo.getIdVehiculo(), null);
    }

    public void Eliminar_Vehiculo(Vehiculo vehiculo) {
        ContentValues values = new ContentValues();
        db.delete(Table_Vehiculo.TABLE, Table_Vehiculo.ID + " = " + vehiculo.getIdVehiculo(), null);
    }

    public Vehiculo get_Vehiculo(int id) {
        try {

            String query = "select * from " + Table_Vehiculo.TABLE +
                    " where " + Table_Vehiculo.ID + " = " + id;
            Cursor cursor = db.rawQuery(query, null);
            Vehiculo vehiculo = null;
            if (cursor.moveToFirst()) {
                do {
                    vehiculo = new Vehiculo();
                    vehiculo.setIdVehiculo(cursor.getInt(0));
                    vehiculo.setNumplaca(cursor.getInt(1));
                    vehiculo.setMarca(cursor.getString(2));
                    vehiculo.setModelo(cursor.getString(3));
                    vehiculo.setMotor(cursor.getString(4));
                    vehiculo.setF_ano(cursor.getString(5));
                    vehiculo.setIdPropietario(cursor.getInt(6));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return vehiculo;

        } catch (Exception ex) {
            return null;
        }


    }

    public ArrayList<Vehiculo> get_all_Vehiculos() {

        ArrayList<Vehiculo> vehiculos = new ArrayList<>();
        try {

            String query = "select * from " + Table_Vehiculo.TABLE;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    Vehiculo vehiculo = new Vehiculo();
                    vehiculo.setIdVehiculo(cursor.getInt(0));
                    vehiculo.setNumplaca(cursor.getInt(1));
                    vehiculo.setMarca(cursor.getString(2));
                    vehiculo.setModelo(cursor.getString(3));
                    vehiculo.setMotor(cursor.getString(4));
                    vehiculo.setF_ano(cursor.getString(5));
                    vehiculo.setIdPropietario(cursor.getInt(6));
                    vehiculos.add(vehiculo);
                } while (cursor.moveToNext());
            }

        } catch (Exception ex) {
            return null;
        }
        return vehiculos;
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
    public Zona get_Zona_Puesto(String idzona) {
        try {
            String query = "SELECT * FROM " + Table_Zona.TABLE +
                    " WHERE " + Table_Zona.ID + " = " + idzona;
            Cursor cursor = db.rawQuery(query, null);
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
    public ArrayList<PuestoControl> get_all_Puesto_Controls_Zona(int idZona){
        ArrayList<PuestoControl> puestoControls = new ArrayList<>();
        try {
            String query = "select * from " + Table_Puesto_Control.TABLE +
                    " WHERE " + Table_Zona.ID + " = " + idZona;
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
    public PuestoControl get_Puesto_Control(int IdPuestoControl) {
        try {
            String query = "SELECT * FROM " + Table_Puesto_Control.TABLE +
                    " WHERE " + Table_Puesto_Control.ID + " = " + IdPuestoControl;
            Cursor cursor = db.rawQuery(query, null);
            PuestoControl puestoControl = null;
            if (cursor.moveToFirst()){
                do {
                    puestoControl = new PuestoControl();
                    puestoControl.setIdPuestoControl(cursor.getInt(0));
                    puestoControl.setIdZona(cursor.getInt(1));
                    puestoControl.setReferencia(cursor.getString(2));
                    puestoControl.setLatitud(cursor.getString(3));
                    puestoControl.setLongitud(cursor.getString(4));
                    puestoControl.setTitulo(cursor.getString(5));
                }while (cursor.moveToNext());
            }
            cursor.close();
            return puestoControl;
        } catch (Exception ex) {
            return null;
        }
    }
    public PuestoControl get_Puesto_Control_Marker(String latitud, String longitud) {
        try {
            String query = "SELECT * FROM " + Table_Puesto_Control.TABLE +
                    " WHERE " + Table_Puesto_Control.LATITUD + " = ? AND " + Table_Puesto_Control.LONGITUD + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{latitud, longitud});
            PuestoControl puestoControl = null;
            if (cursor.moveToFirst()){
                do {
                    puestoControl = new PuestoControl();
                    puestoControl.setIdPuestoControl(cursor.getInt(0));
                    puestoControl.setIdZona(cursor.getInt(1));
                    puestoControl.setReferencia(cursor.getString(2));
                    puestoControl.setLatitud(cursor.getString(3));
                    puestoControl.setLongitud(cursor.getString(4));
                    puestoControl.setTitulo(cursor.getString(5));
                }while (cursor.moveToNext());
            }
            cursor.close();
            return puestoControl;
        } catch (Exception ex) {
            return null;
        }
    }

    public void Actualizar_Puesto_Control(PuestoControl puestoControl) {
        ContentValues values = new ContentValues();
        values.put(Table_Zona.ID, puestoControl.getIdZona());
        values.put(Table_Puesto_Control.REFERENCIA, puestoControl.getReferencia());
        values.put(Table_Puesto_Control.LATITUD, puestoControl.getLatitud());
        values.put(Table_Puesto_Control.LONGITUD, puestoControl.getLongitud());
        values.put(Table_Puesto_Control.TITULO, puestoControl.getTitulo());
        db.update(Table_Puesto_Control.TABLE, values,Table_Puesto_Control.ID + " = " + puestoControl.getIdPuestoControl(),null);
    }
    public void Eliminar_Puesto_Control(PuestoControl puestoControl) {
        ContentValues values = new ContentValues();
        db.delete(Table_Puesto_Control.TABLE,Table_Puesto_Control.ID + " = " + puestoControl.getIdPuestoControl(),null);
    }
    public void Eliminar_Puesto_Control_Zona(int idZona) {
        db.delete(Table_Puesto_Control.TABLE,Table_Zona.ID + " = ?", new String[]{String.valueOf(idZona)});
    }

    public void addActa(Acta acta) {
        ContentValues values = new ContentValues();
        values.put(Table_Acta.CODIGO, acta.getCodigo());
        values.put(Table_Accidente.ID, acta.getIdaccidente());
        values.put(Table_Audiencia.ID, acta.getIdAudiencia());
        values.put(Table_Acta.HORA, acta.getHora());
        values.put(Table_Zona.ID, acta.getIdZona());
        values.put(Table_Agente.ID, acta.getIdagente());
        values.put(Table_Acta.FECHA, acta.getFecha());

        db.insert(Table_Acta.TABLE, null, values);
    }
    public ArrayList<Acta> getAllActas() {
        ArrayList<Acta> actas = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + Table_Acta.TABLE;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    Acta acta = new Acta();
                    acta.setIdActa(cursor.getInt(0));
                    acta.setCodigo(cursor.getInt(1));
                    acta.setIdaccidente(cursor.getInt(2));
                    acta.setIdAudiencia(cursor.getInt(3));
                    acta.setHora(cursor.getString(4));
                    acta.setIdZona(cursor.getInt(5));
                    acta.setIdagente(cursor.getInt(6));
                    acta.setFecha(cursor.getString(7));
                    actas.add(acta);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            return null;
        }
        return actas;
    }
    public Acta get_Acta(int IdActa) {
        try {
            String query = "SELECT * FROM " + Table_Acta.TABLE +
                    " WHERE " + Table_Acta.ID + " = " + IdActa;
            Cursor cursor = db.rawQuery(query, null);
            Acta acta = null;
            if (cursor.moveToFirst()){
                do {
                    acta = new Acta();
                    acta.setIdActa(cursor.getInt(0));
                    acta.setCodigo(cursor.getInt(1));
                    acta.setIdaccidente(cursor.getInt(2));
                    acta.setIdAudiencia(cursor.getInt(3));
                    acta.setHora(cursor.getString(4));
                    acta.setIdZona(cursor.getInt(5));
                    acta.setIdagente(cursor.getInt(6));
                    acta.setFecha(cursor.getString(7));
                }while (cursor.moveToNext());
            }
            cursor.close();
            return acta;
        } catch (Exception ex) {
            return null;
        }
    }
    // Editar Acta
    public int updateActa(Acta acta) {
        ContentValues values = new ContentValues();
        values.put(Table_Acta.CODIGO, acta.getCodigo());
        values.put(Table_Accidente.ID, acta.getIdaccidente());
        values.put(Table_Audiencia.ID, acta.getIdAudiencia());
        values.put(Table_Acta.HORA, acta.getHora());
        values.put(Table_Zona.ID, acta.getIdZona());
        values.put(Table_Agente.ID, acta.getIdagente());
        values.put(Table_Acta.FECHA, acta.getFecha());

        String selection = Table_Acta.ID + " = ?";
        String[] selectionArgs = { String.valueOf(acta.getIdActa()) };

        return db.update(Table_Acta.TABLE, values, selection, selectionArgs);
    }

    // Eliminar Acta
    public int deleteActa(Acta acta) {
        String selection = Table_Acta.ID + " = ?";
        String[] selectionArgs = { String.valueOf(acta.getIdActa()) };

        return db.delete(Table_Acta.TABLE, selection, selectionArgs);
    }

    public void addAgente(Agente agente) {
        ContentValues values = new ContentValues();
        values.put(Table_Agente.CEDULA, agente.getCedulaa());
        values.put(Table_Agente.NOMBRE, agente.getNombre());
        values.put(Table_Puesto_Control.ID, agente.getIdPuestoControl());
        values.put(Table_Agente.RANGO, agente.getRango());

        db.insert(Table_Agente.TABLE, null, values);

    }

    public int updateAgente(Agente agente) {
        ContentValues values = new ContentValues();
        values.put(Table_Agente.CEDULA, agente.getCedulaa());
        values.put(Table_Agente.NOMBRE, agente.getNombre());
        values.put(Table_Puesto_Control.ID, agente.getIdPuestoControl());
        values.put(Table_Agente.RANGO, agente.getRango());
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        int rowsAffected = db.update(Table_Agente.TABLE, values, Table_Agente.ID + " = ?",
                new String[]{String.valueOf(agente.getIdagente())});
        db.close();
        return rowsAffected;
    }

    public void deleteAgente(int idAgente) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        db.delete(Table_Agente.TABLE, Table_Agente.ID + " = ?",
                new String[]{String.valueOf(idAgente)});
        db.close();
    }

    public ArrayList<Agente> getAllAgentes() {
        ArrayList<Agente> agentes = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + Table_Agente.TABLE;
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    Agente agente = new Agente();
                    agente.setIdagente(cursor.getInt(0));
                    agente.setCedulaa(cursor.getInt(1));
                    agente.setNombre(cursor.getString(2));
                    agente.setIdPuestoControl(cursor.getInt(3));
                    agente.setRango(cursor.getString(4));
                    agentes.add(agente);
                } while (cursor.moveToNext());
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return agentes;
    }
    public Agente get_Agente(int IdAgente) {
        try {
            String query = "SELECT * FROM " + Table_Agente.TABLE +
                    " WHERE " + Table_Agente.ID + " = " + IdAgente;
            Cursor cursor = db.rawQuery(query, null);
            Agente agente = null;
            if (cursor.moveToFirst()){
                do {
                    agente = new Agente();
                    agente.setIdagente(cursor.getInt(0));
                    agente.setCedulaa(cursor.getInt(1));
                    agente.setNombre(cursor.getString(2));
                    agente.setIdPuestoControl(cursor.getInt(3));
                    agente.setRango(cursor.getString(4));
                }while (cursor.moveToNext());
            }
            cursor.close();
            return agente;
        } catch (Exception ex) {
            return null;
        }
    }

    //METODOS DE NORMAS DETALLE
    public void Insertar_Normas_Detalle(NormasDet normasDet) {
        ContentValues values = new ContentValues();
        values.put(Table_Norma.NUMERO_NORMA, normasDet.getNumnorma());
        values.put(Table_Norma.DESCRIPCION, normasDet.getDescripcion());
        db.insert(Table_Norma.TABLE, null, values);
    }
    public ArrayList<NormasDet> get_all_Normas_Detalle(){
        ArrayList<NormasDet> normasDets = new ArrayList<>();
        try {
            String query = "select * from " + Table_Norma.TABLE;
            Cursor cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()){
                do {
                    NormasDet normasDet = new NormasDet();
                    normasDet.setIdnomra(cursor.getInt(0));
                    normasDet.setNumnorma(cursor.getString(1));
                    normasDet.setDescripcion(cursor.getString(2));
                    normasDets.add(normasDet);
                }while (cursor.moveToNext());
            }
        } catch (Exception ex){
            return null;
        }
        return normasDets;
    }
    public NormasDet get_Norma_Detalle(int IdNorma) {
        try {
            String query = "SELECT * FROM " + Table_Norma.TABLE +
                    " WHERE " + Table_Norma.ID + " = " + IdNorma;
            Cursor cursor = db.rawQuery(query, null);
            NormasDet normasDet = null;
            if (cursor.moveToFirst()){
                do {
                    normasDet = new NormasDet();
                    normasDet.setIdnomra(cursor.getInt(0));
                    normasDet.setNumnorma(cursor.getString(1));
                    normasDet.setDescripcion(cursor.getString(2));
                }while (cursor.moveToNext());
            }
            cursor.close();
            return normasDet;
        } catch (Exception ex) {
            return null;
        }
    }
    public void Actualizar_Norma_Detalle(NormasDet normasDet) {
        ContentValues values = new ContentValues();
        values.put(Table_Norma.NUMERO_NORMA, normasDet.getNumnorma());
        values.put(Table_Norma.DESCRIPCION, normasDet.getDescripcion());
        db.update(Table_Norma.TABLE, values,Table_Norma.ID + " = " + normasDet.getIdnomra(),null);
    }
    public void Eliminar_Norma_Detalle(NormasDet normasDet) {
        ContentValues values = new ContentValues();
        db.delete(Table_Norma.TABLE,Table_Norma.ID + " = " + normasDet.getIdnomra(),null);
    }
    //METODOS DE IBFRACCION
    public void Insertar_Infraccion(Infraccion infraccion) {
        ContentValues values = new ContentValues();
        values.put(Table_Agente.ID, infraccion.getIdagente());
        values.put(Table_Vehiculo.ID, infraccion.getIdVehiculo());
        values.put(Table_Infraccion.VALOR_MULTA, infraccion.getValormulta());
        values.put(Table_Infraccion.FECHA, infraccion.getFecha());
        values.put(Table_Norma.ID, infraccion.getIdnomra());
        values.put(Table_Infraccion.HORA, infraccion.getHora());
        db.insert(Table_Infraccion.TABLE, null, values);
    }
    public ArrayList<Infraccion> get_all_Infracciones(){
        ArrayList<Infraccion> infraccions = new ArrayList<>();
        try {
            String query = "select * from " + Table_Infraccion.TABLE;
            Cursor cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()){
                do {
                    Infraccion infraccion = new Infraccion();
                    infraccion.setIdInfraccion(cursor.getInt(0));
                    infraccion.setIdagente(cursor.getInt(1));
                    infraccion.setIdVehiculo(cursor.getInt(2));
                    infraccion.setValormulta(cursor.getString(3));
                    infraccion.setFecha(cursor.getString(4));
                    infraccion.setIdnomra(cursor.getInt(5));
                    infraccion.setHora(cursor.getString(6));
                    infraccions.add(infraccion);
                }while (cursor.moveToNext());
            }
        } catch (Exception ex){
            return null;
        }
        return infraccions;
    }
    public Infraccion get_Infraccion(int IdFraccion) {
        try {
            String query = "SELECT * FROM " + Table_Infraccion.TABLE +
                    " WHERE " + Table_Infraccion.ID + " = " + IdFraccion;
            Cursor cursor = db.rawQuery(query, null);
            Infraccion infraccion = null;
            if (cursor.moveToFirst()){
                do {
                    infraccion = new Infraccion();
                    infraccion.setIdInfraccion(cursor.getInt(0));
                    infraccion.setIdagente(cursor.getInt(1));
                    infraccion.setIdVehiculo(cursor.getInt(2));
                    infraccion.setValormulta(cursor.getString(3));
                    infraccion.setFecha(cursor.getString(4));
                    infraccion.setIdnomra(cursor.getInt(5));
                    infraccion.setHora(cursor.getString(6));
                }while (cursor.moveToNext());
            }
            cursor.close();
            return infraccion;
        } catch (Exception ex) {
            return null;
        }
    }
    public void Actualizar_Infraccion(Infraccion infraccion) {
        ContentValues values = new ContentValues();
        values.put(Table_Agente.ID, infraccion.getIdagente());
        values.put(Table_Vehiculo.ID, infraccion.getIdVehiculo());
        values.put(Table_Infraccion.VALOR_MULTA, infraccion.getValormulta());
        values.put(Table_Infraccion.FECHA, infraccion.getFecha());
        values.put(Table_Norma.ID, infraccion.getIdnomra());
        values.put(Table_Infraccion.HORA, infraccion.getHora());
        db.update(Table_Infraccion.TABLE, values,Table_Infraccion.ID + " = " + infraccion.getIdInfraccion(),null);
    }
    public void Eliminar_Infraccion(Infraccion infraccion) {
        ContentValues values = new ContentValues();
        db.delete(Table_Infraccion.TABLE,Table_Infraccion.ID + " = " + infraccion.getIdInfraccion(),null);
    }

    //TABLA OFICINA

    public void Insertar_Oficina(OficinaGob oficinaGob) {
        ContentValues values = new ContentValues();
        values.put(Table_Oficina.VALOR_VEHICULO, oficinaGob.getValor_vehiculo());
        values.put(Table_Oficina.NUMERO_POLIZA, oficinaGob.getNpoliza());
        values.put(Table_Vehiculo.ID, oficinaGob.getIdVehiculo());
        values.put(Table_Oficina.UBICACION, oficinaGob.getUbicacion());
        values.put(Table_Oficina.LATITUD, oficinaGob.getLatitud());
        values.put(Table_Oficina.LONGITUD, oficinaGob.getLongitud());
        db.insert(Table_Oficina.TABLE, null, values);
    }

    public void Actualizar_Oficina(OficinaGob oficinaGob) {
        ContentValues values = new ContentValues();
        values.put(Table_Oficina.VALOR_VEHICULO, oficinaGob.getValor_vehiculo());
        values.put(Table_Oficina.NUMERO_POLIZA, oficinaGob.getNpoliza());
        values.put(Table_Vehiculo.ID, oficinaGob.getIdVehiculo());
        values.put(Table_Oficina.UBICACION, oficinaGob.getUbicacion());
        values.put(Table_Oficina.LATITUD, oficinaGob.getLatitud());
        values.put(Table_Oficina.LONGITUD, oficinaGob.getLongitud());
        db.update(Table_Oficina.TABLE, values, Table_Oficina.ID + " = " + oficinaGob.getIdOficina(), null);
    }

    public void Eliminar_Oficina(OficinaGob oficinaGob) {
        db.delete(Table_Oficina.TABLE, Table_Oficina.ID + " = " + oficinaGob.getIdOficina(), null);
    }

    public OficinaGob get_Oficina(int id) {
        try {
            String query = "SELECT * FROM " + Table_Oficina.TABLE +
                    " WHERE " + Table_Oficina.ID + " = " + id;
            Cursor cursor = db.rawQuery(query, null);
            OficinaGob oficinaGob = null;
            if (cursor.moveToFirst()) {
                do {
                    oficinaGob = new OficinaGob();
                    oficinaGob.setIdOficina(cursor.getInt(0));
                    oficinaGob.setValor_vehiculo(cursor.getString(1));
                    oficinaGob.setNpoliza(cursor.getInt(2));
                    oficinaGob.setIdVehiculo(cursor.getInt(3));
                    oficinaGob.setUbicacion(cursor.getString(4));
                    oficinaGob.setLatitud(cursor.getString(5));
                    oficinaGob.setLongitud(cursor.getString(6));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return oficinaGob;
        } catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<OficinaGob> get_all_Oficinas() {
        ArrayList<OficinaGob> oficinas = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + Table_Oficina.TABLE;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    OficinaGob oficinaGob = new OficinaGob();
                    oficinaGob.setIdOficina(cursor.getInt(0));
                    oficinaGob.setValor_vehiculo(cursor.getString(1));
                    oficinaGob.setNpoliza(cursor.getInt(2));
                    oficinaGob.setIdVehiculo(cursor.getInt(3));
                    oficinaGob.setUbicacion(cursor.getString(4));
                    oficinaGob.setLatitud(cursor.getString(5));
                    oficinaGob.setLongitud(cursor.getString(6));
                    oficinas.add(oficinaGob);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception ex) {
            return null;
        }
        return oficinas;
    }
}
