package com.example.grupo_05_tarea_16_ejercicio_01.modelo;

public class OficinaGob {
    private int IdOficina;
    private String valor_vehiculo;
    private int npoliza;
    private int IdVehiculo;
    private String ubicacion;
    private double latitud;
    private double longitud;

    public OficinaGob() {
    }

    public OficinaGob(String valor_vehiculo, int npoliza, int idVehiculo, String ubicacion, double latitud, double longitud) {
        this.valor_vehiculo = valor_vehiculo;
        this.npoliza = npoliza;
        IdVehiculo = idVehiculo;
        this.ubicacion = ubicacion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getIdOficina() {
        return IdOficina;
    }

    public void setIdOficina(int idOficina) {
        IdOficina = idOficina;
    }

    public String getValor_vehiculo() {
        return valor_vehiculo;
    }

    public void setValor_vehiculo(String valor_vehiculo) {
        this.valor_vehiculo = valor_vehiculo;
    }

    public int getNpoliza() {
        return npoliza;
    }

    public void setNpoliza(int npoliza) {
        this.npoliza = npoliza;
    }

    public int getIdVehiculo() {
        return IdVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.IdVehiculo = idVehiculo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
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
}
