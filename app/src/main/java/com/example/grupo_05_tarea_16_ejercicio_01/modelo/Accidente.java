package com.example.grupo_05_tarea_16_ejercicio_01.modelo;

public class Accidente {
    private int Idaccidente;
    private int IdVehiculo;
    private int Idagente;
    private String hora;
    private String fecha;
    private String descripcion;

    public Accidente() {
    }

    public Accidente(int idVehiculo, int idagente, String hora, String fecha, String descripcion) {
        this.IdVehiculo = idVehiculo;
        this.Idagente = idagente;
        this.hora = hora;
        this.fecha = fecha;
        this.descripcion = descripcion;
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
}
