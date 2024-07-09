package com.example.grupo_05_tarea_16_ejercicio_01.db;

import android.content.Context;

import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;

public class DBHelper {
    private DBAdapter dbAdapter;

    public DBHelper(Context context) {

        dbAdapter = new DBAdapter(context);
    }

    public void Insertar_Zonas(Zona zona){
        dbAdapter.open();
        dbAdapter.Insertar_Zona(zona);
        dbAdapter.close();
    }
}
