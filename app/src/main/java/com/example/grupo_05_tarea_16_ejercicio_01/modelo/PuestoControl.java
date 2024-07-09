package com.example.grupo_05_tarea_16_ejercicio_01.modelo;

public class PuestoControl {
    private int IdPuestoControl;
    private int IdZona;
    private String ubicacion;

    public PuestoControl() {
    }

    public PuestoControl(int idZona, String ubicacion) {
        this.IdZona = idZona;
        this.ubicacion = ubicacion;
    }

    public int getIdPuestoControl() {
        return IdPuestoControl;
    }

    public void setIdPuestoControl(int idPuestoControl) {
        this.IdPuestoControl = idPuestoControl;
    }

    public int getIdZona() {
        return IdZona;
    }

    public void setIdZona(int idZona) {
        this.IdZona = idZona;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
