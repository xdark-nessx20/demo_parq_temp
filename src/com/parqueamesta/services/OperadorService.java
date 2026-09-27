package com.parqueamesta.services;

import com.parqueamesta.model.Operador;
import com.parqueamesta.model.Rol;
import com.parqueamesta.persistence.UsuarioRepository;

import java.util.List;
import java.util.Optional;

public class OperadorService {
    private final UsuarioRepository repo = new UsuarioRepository();

    public boolean save(String nombre, String cedula, String contrasena) {
        if (nombreInvalido(nombre)) return false;
        if (cedulaInvalida(cedula)) return false;
        if (contrasenaInvalida(contrasena)) return false;

        return repo.save(new Operador(nombre, cedula), contrasena);
    }

    public Optional<Operador> findByCedula(String cedula) {
        if (cedulaInvalida(cedula)) return Optional.empty();

        return repo.get(cedula)
                .filter(u -> u.rol() == Rol.OPERADOR)
                .map(u -> (Operador) u);
    }

    public List<Operador> findAll() {
        return repo.getByRol(Rol.OPERADOR).stream().map(u -> (Operador) u).toList();
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
        return contrasena == null || contrasena.length() < 6;
    }
}
