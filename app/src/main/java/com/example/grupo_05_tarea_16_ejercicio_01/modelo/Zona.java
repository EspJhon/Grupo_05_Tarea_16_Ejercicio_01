package com.example.grupo_05_tarea_16_ejercicio_01.modelo;

public class Zona {
    private int IdZona;
    private String departamento;
    private String provincia;
    private String distrito;
    private String latitud;
    private String longitud;
    private String titulo;

    public Zona() {
    }

    public Zona(String departamento, String provincia, String distrito, String latitud, String longitud, String titulo) {
        this.departamento = departamento;
        this.provincia = provincia;
        this.distrito = distrito;
        this.latitud = latitud;
        this.longitud = longitud;
        this.titulo = titulo;
    }

    public int getIdZona() {
        return IdZona;
    }

    public void setIdZona(int idZona) {
        this.IdZona = idZona;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
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
