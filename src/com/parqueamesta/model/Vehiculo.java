package com.parqueamesta.model;

import java.util.Objects;
import java.util.UUID;

public class Vehiculo {
    private UUID id;
    private String placa;
    private Cliente owner;
    private TipoVehiculo tipo;

    public Vehiculo(UUID id, String placa, Cliente owner, TipoVehiculo tipo) {
        this.id = id;
        this.placa = placa;
        this.owner = owner;
        this.tipo = tipo;
    }

    public Vehiculo(String placa, Cliente owner, TipoVehiculo tipo) {
        this.placa = placa;
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

    public Cliente owner() {
        return owner;
    }

    public TipoVehiculo tipo() {
        return tipo;
    }

    // Getters JavaBean (para JSP/EL)
    public UUID getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public Cliente getOwner() {
        return owner;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    //Setters

    public void setPlaca(String placa) {
        this.placa = placa;
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
        return "Vehiculo {id: %s, placa: %s, owner: %s, tipo: %s}"
                .formatted(id != null ? id.toString() : "", placa,
                        owner != null ? owner.toString() : "", tipo != null ? tipo.toString() : "");
    }
}
