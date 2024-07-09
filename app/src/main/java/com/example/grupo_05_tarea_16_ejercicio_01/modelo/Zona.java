package com.example.grupo_05_tarea_16_ejercicio_01.modelo;

public class Zona {
    private int IdZona;
    private String ubicacion;

    public Zona() {
    }

    public Zona(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getIdActa() {
        return IdZona;
    }

    public void setIdActa(int idActa) {
        IdZona = idActa;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
