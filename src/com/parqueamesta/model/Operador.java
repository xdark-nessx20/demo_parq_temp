package com.parqueamesta.model;

import java.util.UUID;

public class Operador extends Usuario {

    public Operador(UUID id, String nombre, String cedula) {
        super(id, nombre, cedula);
    }

    public Operador(String nombre, String cedula) {
        super(nombre, cedula);
    }

    @Override
    public Rol rol() {
        return Rol.OPERADOR;
    }
}
