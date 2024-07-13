package com.example.grupo_05_tarea_16_ejercicio_01.modelo;

import androidx.annotation.NonNull;

public class Audiencia {
    private int IdAudiencia;
    private int codigo;
    private String lugar;
    private String fecha;
    private String hora;

    public Audiencia() {
    }

    public Audiencia(int codigo, String lugar, String fecha, String hora) {
        this.codigo = codigo;
        this.lugar = lugar;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getIdAudiencia() {
        return IdAudiencia;
    }

    public void setIdAudiencia(int idAudiencia) {
        this.IdAudiencia = idAudiencia;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    @NonNull
    @Override
    public String toString() {
        return getIdAudiencia() + " ----  " + getCodigo() + " ------ " + getLugar() + " ------ " +
                getFecha() + " ------ " + getHora();
    }
}
