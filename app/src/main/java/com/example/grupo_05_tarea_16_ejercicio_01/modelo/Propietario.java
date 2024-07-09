package com.example.grupo_05_tarea_16_ejercicio_01.modelo;

public class Propietario {

    private int IdPropietario;
    private int cedulap;
    private String nombre;
    private String ciudad;

    public Propietario() {
    }

    public Propietario(int cedulap, String nombre, String ciudad) {
        this.cedulap = cedulap;
        this.nombre = nombre;
        this.ciudad = ciudad;
    }

    public int getIdPropietario() {
        return IdPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        IdPropietario = idPropietario;
    }

    public int getCedulap() {
        return cedulap;
    }

    public void setCedulap(int cedulap) {
        this.cedulap = cedulap;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
