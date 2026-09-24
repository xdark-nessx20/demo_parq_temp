package com.parqueamesta.model;

public class Cliente extends Usuario{
    private Rol rol;

    public Cliente() {
        super();
        rol = Rol.CLIENTE;
    }

    public Rol rol() {
        return rol;
    }
}
