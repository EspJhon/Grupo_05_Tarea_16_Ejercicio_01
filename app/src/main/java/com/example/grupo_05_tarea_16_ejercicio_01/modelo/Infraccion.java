package com.example.grupo_05_tarea_16_ejercicio_01.modelo;

public class Infraccion {
    private int IdInfraccion;
    private int Idagente;
    private int IdVehiculo;
    private String valormulta;
    private String fecha;
    private int Idnomra;
    private String hora;

    public Infraccion() {
    }

    public Infraccion(int idagente, int idVehiculo, String valormulta, String fecha, int idnomra, String hora) {
        this.Idagente = idagente;
        this.IdVehiculo = idVehiculo;
        this.valormulta = valormulta;
        this.fecha = fecha;
        this.Idnomra = idnomra;
        this.hora = hora;
    }

    public int getIdInfraccion() {
        return IdInfraccion;
    }

    public void setIdInfraccion(int idInfraccion) {
        this.IdInfraccion = idInfraccion;
    }

    public int getIdagente() {
        return Idagente;
    }

    public void setIdagente(int idagente) {
        this.Idagente = idagente;
    }

    public int getIdVehiculo() {
        return IdVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.IdVehiculo = idVehiculo;
    }

    public String getValormulta() {
        return valormulta;
    }

    public void setValormulta(String valormulta) {
        this.valormulta = valormulta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdnomra() {
        return Idnomra;
    }

    public void setIdnomra(int idnomra) {
        this.Idnomra = idnomra;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
