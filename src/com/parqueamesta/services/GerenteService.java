package com.parqueamesta.services;

import com.parqueamesta.model.Gerente;
import com.parqueamesta.model.Rol;
import com.parqueamesta.persistence.UsuarioRepository;

import java.util.List;
import java.util.Optional;

public class GerenteService {
    private final UsuarioRepository repo = new UsuarioRepository();

    public boolean save(String nombre, String cedula, String contrasena) {
        if (nombreInvalido(nombre)) return false;
        if (cedulaInvalida(cedula)) return false;
        if (contrasenaInvalida(contrasena)) return false;

        return repo.save(new Gerente(nombre, cedula), contrasena);
    }

    public Optional<Gerente> findByCedula(String cedula) {
        if (cedulaInvalida(cedula)) return Optional.empty();

        return repo.get(cedula)
                .filter(u -> u.rol() == Rol.GERENTE)
                .map(u -> (Gerente) u);
    }

    public List<Gerente> findAll() {
        return repo.getByRol(Rol.GERENTE).stream().map(u -> (Gerente) u).toList();
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
        return cedula == null || !cedula.matches("^[1-9][0-9]{7}([0-9]{2})?");
    }

    private boolean contrasenaInvalida(String contrasena) {
        return !com.parqueamesta.util.PasswordPolicy.valida(contrasena);
    }
}
