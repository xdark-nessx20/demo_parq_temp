package com.parqueamesta.services;

import com.parqueamesta.model.Cliente;
import com.parqueamesta.model.Rol;
import com.parqueamesta.persistence.UsuarioRepository;

import java.util.List;
import java.util.Optional;

public class ClienteService {
    private final UsuarioRepository repo = new UsuarioRepository();

    public boolean save(String nombre, String cedula) {
        if (nombreInvalido(nombre)) return false;
        if (cedulaInvalida(cedula)) return false;

        return repo.save(new Cliente(nombre, cedula), null);
    }

    public Optional<Cliente> findByCedula(String cedula) {
        if (cedulaInvalida(cedula)) return Optional.empty();

        return repo.get(cedula)
                .filter(u -> u.rol() == Rol.CLIENTE)
                .map(u -> (Cliente) u);
    }

    public List<Cliente> findAll() {
        return repo.getByRol(Rol.CLIENTE).stream().map(u -> (Cliente) u).toList();
    }

    private boolean nombreInvalido(String nombre) {
        return nombre == null || nombre.isBlank() || nombre.length() < 6;
    }

    private boolean cedulaInvalida(String cedula) {
        return cedula == null || !cedula.matches("^[1-9][0-9]{7}([0-9]{2})?");
    }
}
