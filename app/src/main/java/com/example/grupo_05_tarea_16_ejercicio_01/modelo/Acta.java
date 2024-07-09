package com.example.grupo_05_tarea_16_ejercicio_01.modelo;

public class Acta {
    private int IdActa;
    private int codigo;
    private int Idaccidente;
    private int IdAudiencia;
    private String hora;
    private int IdZona;
    private int Idagente;
    private String fecha;

    public Acta() {
    }

    public Acta(int codigo, int idaccidente, int idAudiencia, String hora, int idZona, int idagente, String fecha) {
        this.codigo = codigo;
        this.Idaccidente = idaccidente;
        this.IdAudiencia = idAudiencia;
        this.hora = hora;
        this.IdZona = idZona;
        this.Idagente = idagente;
        this.fecha = fecha;
    }

    public int getIdActa() {
        return IdActa;
    }

    public void setIdActa(int idActa) {
        this.IdActa = idActa;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getIdaccidente() {
        return Idaccidente;
    }

    public void setIdaccidente(int idaccidente) {
        this.Idaccidente = idaccidente;
    }

    public int getIdAudiencia() {
        return IdAudiencia;
    }

    public void setIdAudiencia(int idAudiencia) {
        this.IdAudiencia = idAudiencia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getIdZona() {
        return IdZona;
    }

    public void setIdZona(int idZona) {
        this.IdZona = idZona;
    }

    public int getIdagente() {
        return Idagente;
    }

    public void setIdagente(int idagente) {
        this.Idagente = idagente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
