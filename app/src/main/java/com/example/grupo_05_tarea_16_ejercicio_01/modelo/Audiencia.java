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
        setFecha(fecha); // Usar los métodos de validación
        setHora(hora);   // Usar los métodos de validación
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
        if (!isValidDate(fecha)) {
            throw new IllegalArgumentException("Formato de fecha incorrecto, debe ser YYYY-MM-DD");
        }
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        if (!isValidTime(hora)) {
            throw new IllegalArgumentException("Formato de hora incorrecto, debe ser HH:MM:SS");
        }
        this.hora = hora;
    }

    @NonNull
    @Override
    public String toString() {
        return getIdAudiencia() + " ----  " + getCodigo() + " ------ " + getLugar() + " ------ " +
                getFecha() + " ------ " + getHora();
    }

    private boolean isValidDate(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private boolean isValidTime(String time) {
        return time.matches("\\d{2}:\\d{2}:\\d{2}");
    }
}
