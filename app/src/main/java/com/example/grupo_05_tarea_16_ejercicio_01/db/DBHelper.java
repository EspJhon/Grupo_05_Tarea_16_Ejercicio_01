package com.example.grupo_05_tarea_16_ejercicio_01.db;

import android.content.Context;

import com.example.grupo_05_tarea_16_ejercicio_01.modelo.PuestoControl;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;

import java.util.ArrayList;

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
    public ArrayList<Zona> get_all_Zonas(){
        dbAdapter.open();
        ArrayList<Zona> zonas = dbAdapter.get_all_Zonas();
        dbAdapter.close();
        return zonas;
    }
    public Zona get_Zona(String latitud, String longitud){
        dbAdapter.open();
        Zona zona = dbAdapter.get_Zona(latitud, longitud);
        dbAdapter.close();
        return zona;
    }
    public void Eliminar_Zona(Zona zona) {
        dbAdapter.open();
        dbAdapter.Eliminar_Zona(zona);
        dbAdapter.close();
    }
    public void Actualizar_Ubicacion(Zona zona) {
        dbAdapter.open();
        dbAdapter.Actualizar_Zona(zona);
        dbAdapter.close();
    }
    public void Insertar_Puesto_Control(PuestoControl puestoControl){
        dbAdapter.open();
        dbAdapter.Insertar_Puesto_Control(puestoControl);
        dbAdapter.close();
    }
    public ArrayList<PuestoControl> get_all_Puesto_Controls(){
        dbAdapter.open();
        ArrayList<PuestoControl> puestoControls = dbAdapter.get_all_Puesto_Controls();
        dbAdapter.close();
        return puestoControls;
    }
}
