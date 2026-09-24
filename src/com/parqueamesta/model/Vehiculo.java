package com.parqueamesta.model;

import java.util.Objects;
import java.util.UUID;

public class Vehiculo {
    private UUID id;
    private String placa;
    private String marca;
    private Cliente owner;
    private TipoVehiculo tipo;

    public Vehiculo(UUID id, String placa, String marca, Cliente owner, TipoVehiculo tipo) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.owner = owner;
        this.tipo = tipo;
    }

    public Vehiculo(String placa, String marca, Cliente owner, TipoVehiculo tipo) {
        this.placa = placa;
        this.marca = marca;
        this.owner = owner;
        this.tipo = tipo;
    }

    //Getters con record style
    public UUID id() {
        return id;
    }

    public String placa() {
        return placa;
    }

    public String marca() {
        return marca;
    }

    public Cliente owner() {
        return owner;
    }

    public TipoVehiculo tipo() {
        return tipo;
    }

    //Setters

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setOwner(Cliente owner) {
        this.owner = owner;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vehiculo vehiculo)) return false;
        return Objects.equals(id, vehiculo.id) && Objects.equals(placa, vehiculo.placa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, placa);
    }

    @Override
    public String toString() {
        return "Vehiculo {id: %s, placa: %s, marca: %s, owner: %s, tipo: %s}"
                .formatted(id.toString(), placa, marca,
                        owner != null ? owner.toString() : "", tipo != null ? tipo.toString() : "");
    }
}
