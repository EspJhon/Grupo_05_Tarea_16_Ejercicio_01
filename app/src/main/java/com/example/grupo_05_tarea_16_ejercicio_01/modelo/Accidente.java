package com.example.grupo_05_tarea_16_ejercicio_01.modelo;

import androidx.annotation.NonNull;

public class Accidente {
    private int Idaccidente;
    private int IdVehiculo;
    private int Idagente;
    private String hora;
    private String fecha;
    private String titulo;
    private String descripcion;
    private String URLimagen;
    private String nombreLugar;
    private double latitud;
    private double longitud;

    public Accidente() {
    }

    public Accidente(int idVehiculo, int idagente, String hora, String fecha, String titulo, String descripcion,
                     String URLimagen, String nombreLugar, double latitud, double longitud) {
        IdVehiculo = idVehiculo;
        Idagente = idagente;
        this.hora = hora;
        this.fecha = fecha;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.URLimagen = URLimagen;
        this.nombreLugar = nombreLugar;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getIdaccidente() {
        return Idaccidente;
    }

    public void setIdaccidente(int idaccidente) {
        this.Idaccidente = idaccidente;
    }

    public int getIdVehiculo() {
        return IdVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.IdVehiculo = idVehiculo;
    }

    public int getIdagente() {
        return Idagente;
    }

    public void setIdagente(int idagente) {
        this.Idagente = idagente;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getURLimagen() {
        return URLimagen;
    }

    public void setURLimagen(String URLimagen) {
        this.URLimagen = URLimagen;
    }

    public String getNombreLugar() {
        return nombreLugar;
    }

    public void setNombreLugar(String nombreLugar) {
        this.nombreLugar = nombreLugar;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    @NonNull
    @Override
    public String toString() {
        return "Accidente{" +
                "Idaccidente=" + Idaccidente +
                ", IdVehiculo=" + IdVehiculo +
                ", Idagente=" + Idagente +
                ", hora='" + hora + '\'' +
                ", fecha='" + fecha + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", URLimagen='" + URLimagen + '\'' +
                ", nombreLugar='" + nombreLugar + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }

}
