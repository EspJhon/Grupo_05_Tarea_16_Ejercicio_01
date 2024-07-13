package com.example.grupo_05_tarea_16_ejercicio_01.db;

import android.content.Context;

import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Audiencia;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Propietario;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.PuestoControl;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Usuario;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Vehiculo;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;

import java.util.ArrayList;

public class DBHelper {
    private DBAdapter dbAdapter;

    public DBHelper(Context context) {

        dbAdapter = new DBAdapter(context);
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

    //USUARIO

    public void insertarUsuario(Usuario usuario) {
        dbAdapter.open();
        dbAdapter.insertarUsuario(usuario);
        dbAdapter.close();
    }

    public boolean validarUsuario(Usuario usuario) {
        dbAdapter.open();
        boolean valido = dbAdapter.validarUsuario(usuario);
        dbAdapter.close();
        return valido;
    }

    //PROPIETARIO

    public void Insertar_Propietario(Propietario propietario) {
        dbAdapter.open();
        dbAdapter.insertarPropietario(propietario);
        dbAdapter.close();
    }

    public ArrayList<Propietario> get_all_Propietarios() {
        dbAdapter.open();
        ArrayList<Propietario> propietarios = dbAdapter.get_all_Propietarios();
        dbAdapter.close();
        return propietarios;
    }

    public void Actualizar_Propietario(Propietario propietario) {
        dbAdapter.open();
        dbAdapter.actualizarPropietario(propietario);
        dbAdapter.close();
    }

    public void Eliminar_Propietario(Propietario propietario) {
        dbAdapter.open();
        dbAdapter.Eliminar_Propietario(propietario);
        dbAdapter.close();
    }

    public Propietario get_Propietario(int id) {
        dbAdapter.open();
        Propietario propietario = dbAdapter.get_Propietario(id);
        dbAdapter.close();
        return propietario;
    }

    // VEHICULO

    public void Insertar_Vehiculo(Vehiculo vehiculo) {
        dbAdapter.open();
        dbAdapter.Insertar_Vehiculo(vehiculo);
        dbAdapter.close();
    }

    public ArrayList<Vehiculo> get_all_Vehiculos() {
        dbAdapter.open();
        ArrayList<Vehiculo> vehiculos = dbAdapter.get_all_Vehiculos();
        dbAdapter.close();
        return vehiculos;
    }

    public void Actualizar_Vehiculo(Vehiculo vehiculo) {
        dbAdapter.open();
        dbAdapter.Actualizar_Vehiculo(vehiculo);
        dbAdapter.close();
    }

    public void Eliminar_Vehiculo(Vehiculo vehiculo) {
        dbAdapter.open();
        dbAdapter.Eliminar_Vehiculo(vehiculo);
        dbAdapter.close();
    }

    public Vehiculo get_Vehiculo(int id) {
        dbAdapter.open();
        Vehiculo vehiculo = dbAdapter.get_Vehiculo(id);
        dbAdapter.close();
        return vehiculo;
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
