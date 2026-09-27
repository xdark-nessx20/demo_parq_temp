package com.parqueamesta.services;

import com.parqueamesta.model.Cliente;
import com.parqueamesta.model.Rol;
import com.parqueamesta.persistence.UsuarioRepository;

import java.util.List;
import java.util.Optional;

public class ClienteService {
    private final UsuarioRepository repo = new UsuarioRepository();

    public boolean save(String nombre, String cedula, String contrasena) {
        if (nombreInvalido(nombre)) return false;
        if (cedulaInvalida(cedula)) return false;
        if (contrasenaInvalida(contrasena)) return false;

        return repo.save(new Cliente(nombre, cedula), contrasena);
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

    private boolean contrasenaInvalida(String contrasena) {
        return contrasena == null || contrasena.length() < 6;
    }
}
