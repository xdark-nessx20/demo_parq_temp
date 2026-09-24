package com.parqueamesta.model;

import java.util.UUID;

public class Cliente extends Usuario {
    private Rol rol;

    public Cliente(UUID id, String nombre) {
        super(id, nombre);
        this.rol = Rol.CLIENTE;
    }

    public Rol rol() {
        return rol;
    }
}
