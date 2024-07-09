package com.example.grupo_05_tarea_16_ejercicio_01.modelo;

public class Vehiculo {

    private int IdVehiculo;
    private int numplaca;
    private String marca;
    private String modelo;
    private String motor;
    private String f_ano;
    private int IdPropietario;

    public Vehiculo() {
    }

    public Vehiculo(int numplaca, String marca, String modelo, String motor, String f_ano, int idPropietario) {
        this.numplaca = numplaca;
        this.marca = marca;
        this.modelo = modelo;
        this.motor = motor;
        this.f_ano = f_ano;
        this.IdPropietario = idPropietario;
    }

    public int getIdVehiculo() {
        return IdVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        IdVehiculo = idVehiculo;
    }

    public int getNumplaca() {
        return numplaca;
    }

    public void setNumplaca(int numplaca) {
        this.numplaca = numplaca;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getF_ano() {
        return f_ano;
    }

    public void setF_ano(String f_ano) {
        this.f_ano = f_ano;
    }

    public int getIdPropietario() {
        return IdPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        this.IdPropietario = idPropietario;
    }
}
