package com.example.grupo_05_tarea_16_ejercicio_01.modelo;

public class NormasDet {
    private int Idnomra;
    private String numnorma;
    private String descripcion;

    public NormasDet() {
    }

    public NormasDet(String numnorma, String descripcion) {
        this.numnorma = numnorma;
        this.descripcion = descripcion;
    }

    public int getIdnomra() {
        return Idnomra;
    }

    public void setIdnomra(int idnomra) {
        this.Idnomra = idnomra;
    }

    public String getNumnorma() {
        return numnorma;
    }

    public void setNumnorma(String numnorma) {
        this.numnorma = numnorma;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
