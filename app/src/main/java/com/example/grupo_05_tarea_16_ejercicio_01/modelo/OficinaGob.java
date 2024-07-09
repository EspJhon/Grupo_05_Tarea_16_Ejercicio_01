package com.example.grupo_05_tarea_16_ejercicio_01.modelo;

public class OficinaGob {
    private int IdOficina;
    private String valor_vehiculo;
    private int npoliza;
    private int IdVehiculo;
    private String ubicacion;

    public OficinaGob() {
    }

    public OficinaGob(String valor_vehiculo, int npoliza, int idVehiculo, String ubicacion) {
        this.valor_vehiculo = valor_vehiculo;
        this.npoliza = npoliza;
        this.IdVehiculo = idVehiculo;
        this.ubicacion = ubicacion;
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
}
