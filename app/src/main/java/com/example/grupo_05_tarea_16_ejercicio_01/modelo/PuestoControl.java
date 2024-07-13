package com.example.grupo_05_tarea_16_ejercicio_01.modelo;

public class PuestoControl {
    private int IdPuestoControl;
    private int IdZona;
    private String referencia;
    private String latitud;
    private String longitud;
    private String titulo;

    public PuestoControl() {
    }

    public PuestoControl(int idZona, String referencia, String latitud, String longitud, String titulo) {
        IdZona = idZona;
        this.referencia = referencia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.titulo = titulo;
    }

    public int getIdPuestoControl() {
        return IdPuestoControl;
    }

    public void setIdPuestoControl(int idPuestoControl) {
        IdPuestoControl = idPuestoControl;
    }

    public int getIdZona() {
        return IdZona;
    }

    public void setIdZona(int idZona) {
        IdZona = idZona;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        referencia = referencia;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
