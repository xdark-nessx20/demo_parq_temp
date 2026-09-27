package com.parqueamesta.model;

import java.util.Objects;
import java.util.UUID;

public class TipoVehiculo {
    private UUID id;
    private String nombre;

    public TipoVehiculo(UUID id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public TipoVehiculo(String nombre) {
        this.nombre = nombre;
    }

    //Getters con record style
    public UUID id() {
        return id;
    }

    public String nombre() {
        return nombre;
    }

    // Getters JavaBean (para JSP/EL)
    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    //Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        return "TipoVehiculo {id: %s, nombre: %s}"
                .formatted(id != null ? id.toString() : "", nombre);
    }
}
