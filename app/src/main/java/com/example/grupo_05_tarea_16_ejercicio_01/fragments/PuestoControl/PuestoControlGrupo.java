package com.example.grupo_05_tarea_16_ejercicio_01.fragments.PuestoControl;

public class PuestoControlGrupo {
    private String idZona;
    private int totalPuestos;

    public PuestoControlGrupo(String idZona, int totalPuestos) {
        this.idZona = idZona;
        this.totalPuestos = totalPuestos;
    }

    public String getIdZona() {
        return idZona;
    }

    public int getTotalPuestos() {
        return totalPuestos;
    }
}
