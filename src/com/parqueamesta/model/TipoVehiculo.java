package com.parqueamesta.model;

import java.util.Objects;
import java.util.UUID;

public class TipoVehiculo {
    private UUID id;
    private String nombre;
    private String descripcion;

    public TipoVehiculo(UUID id, String name, String descripcion) {
        this.id = id;
        this.nombre = name;
        this.descripcion = descripcion;
    }

    public TipoVehiculo(String name, String descripcion) {
        this.nombre = name;
        this.descripcion = descripcion;
    }

    //Getters con record style

    public UUID id() {
        return id;
    }

    public String nombre() {
        return nombre;
    }

    public String descripcion() {
        return descripcion;
    }

    //Setters

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TipoVehiculo that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }

    @Override
    public String toString() {
        return "TipoVehiculo {id: %s, nombre: %s, descripcion: %s}".formatted(id.toString(), nombre, descripcion);
    }
}
