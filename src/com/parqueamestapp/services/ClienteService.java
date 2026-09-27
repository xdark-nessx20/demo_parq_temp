package com.parqueamestapp.services;

import com.parqueamestapp.model.Cliente;
import com.parqueamestapp.model.Rol;
import com.parqueamestapp.persistence.UsuarioRepository;

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

    public boolean update(String cedula, String nombre) {
        if (nombreInvalido(nombre)) return false;
        if (cedulaInvalida(cedula)) return false;
        return repo.update(cedula, nombre);
    }

    public boolean delete(String cedula) {
        if (cedulaInvalida(cedula)) return false;
        return repo.delete(cedula);
    }

    public boolean existeCedula(String cedula) {
        return cedula != null && repo.get(cedula).isPresent();
    }

    private boolean nombreInvalido(String nombre) {
        return nombre == null || nombre.isBlank() || nombre.length() < 6;
    }

    private boolean cedulaInvalida(String cedula) {
        return cedula == null || !cedula.matches("^[1-9][0-9]{7,9}$");
    }

    private boolean contrasenaInvalida(String contrasena) {
        return !com.parqueamestapp.util.PasswordPolicy.valida(contrasena);
    }
}
