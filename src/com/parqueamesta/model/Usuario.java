package com.parqueamesta.model;

import java.util.Objects;
import java.util.UUID;

public abstract class Usuario {
    private UUID id;
    private String nombre;
    private String cedula;

    protected Usuario(String nombre, String cedula) {
        this.nombre = nombre;
        this.cedula = cedula;
    }

    protected Usuario(UUID id, String nombre, String cedula) {
        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
    }

    public UUID id() {
        return id;
    }

    public String nombre() {
        return nombre;
    }

    public String cedula() {
        return cedula;
    }

    public abstract Rol rol();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Usuario usuario)) return false;
        return Objects.equals(id, usuario.id) && Objects.equals(cedula, usuario.cedula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cedula);
    }

    @Override
    public String toString() {
        return "%s {id: %s, nombre: %s, cedula: %s}"
                .formatted(getClass().getSimpleName(),
                        id != null ? id.toString() : "",
                        nombre,
                        cedula);
    }
}
