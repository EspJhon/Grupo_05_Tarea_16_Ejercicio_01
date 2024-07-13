package com.example.grupo_05_tarea_16_ejercicio_01.db;

import android.content.Context;

import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Audiencia;
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


    //MÉTODOS TABLA ACCIDENTE

    public void Insertar_Accidente(Accidente accidente){
        dbAdapter.open();
        dbAdapter.Insertar_Accidente(accidente);
        dbAdapter.close();
    }

    public void Actualizar_Accidente(Accidente accidente){
        dbAdapter.open();
        dbAdapter.Actualizar_Accidente(accidente);
        dbAdapter.close();
    }

    public void Eliminar_Accidente(Accidente accidente){
        dbAdapter.open();
        dbAdapter.Eliminar_Accidente(accidente);
        dbAdapter.close();
    }

    public Accidente get_Accidente(int id){
        dbAdapter.open();
        Accidente accidente = dbAdapter.get_Accidente(id);
        dbAdapter.close();
        return accidente;
    }

    public ArrayList<Accidente> get_all_Accidentes(){
        dbAdapter.open();
        ArrayList<Accidente> accidentes = dbAdapter.get_all_Accidente();
        dbAdapter.close();
        return accidentes;
    }


    //MÉTODOS TABLA AUDIENCIA

    public void Insertar_Audiencia(Audiencia audiencia){
        dbAdapter.open();
        dbAdapter.Insertar_Audiencia(audiencia);
        dbAdapter.close();
    }

    public void Actualizar_Audiencia(Audiencia audiencia){
        dbAdapter.open();
        dbAdapter.Actualizar_Audiencia(audiencia);
        dbAdapter.close();
    }

    public void Eliminar_Audiencia(Audiencia audiencia){
        dbAdapter.open();
        dbAdapter.Eliminar_Audiencia(audiencia);
        dbAdapter.close();
    }

    public Audiencia get_Audiencia(int id){
        dbAdapter.open();
        Audiencia audiencia = dbAdapter.get_Audiencia(id);
        dbAdapter.close();
        return audiencia;
    }

    public ArrayList<Audiencia> get_all_Audiencias(){
        dbAdapter.open();
        ArrayList<Audiencia> audiencias = dbAdapter.get_all_Audiencia();
        dbAdapter.close();
        return audiencias;
    }
}
