package com.parqueamesta.model;

import java.util.UUID;

public class Cliente extends Usuario {
    private Rol rol;

    public Cliente(UUID id, String nombre, String cedula) {
        super(id, nombre, cedula);
        this.rol = Rol.CLIENTE;
    }

    public Cliente(String nombre, String cedula) {
        super(nombre, cedula);
        this.rol = Rol.CLIENTE;
    }

    public Rol rol() {
        return rol;
    }
}
