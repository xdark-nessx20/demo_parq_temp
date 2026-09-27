package com.parqueamesta.model;

import java.util.UUID;

public class Gerente extends Usuario {

    public Gerente(UUID id, String nombre, String cedula) {
        super(id, nombre, cedula);
    }

    public Gerente(String nombre, String cedula) {
        super(nombre, cedula);
    }

    @Override
    public Rol rol() {
        return Rol.GERENTE;
    }
}
