package com.example.grupo_05_tarea_16_ejercicio_01.modelo;

public class Agente {
    private int Idagente;
    private int cedulaa;
    private String Nombre;
    private int IdPuestoControl;
    private String rango;

    public Agente() {
    }

    public Agente(int cedulaa, String nombre, int idPuestoControl, String rango) {
        this.cedulaa = cedulaa;
        this.Nombre = nombre;
        this.IdPuestoControl = idPuestoControl;
        this.rango = rango;
    }

    public int getIdagente() {
        return Idagente;
    }

    public void setIdagente(int idagente) {
        this.Idagente = idagente;
    }

    public int getCedulaa() {
        return cedulaa;
    }

    public void setCedulaa(int cedulaa) {
        this.cedulaa = cedulaa;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public int getIdPuestoControl() {
        return IdPuestoControl;
    }

    public void setIdPuestoControl(int idPuestoControl) {
        this.IdPuestoControl = idPuestoControl;
    }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }
}

