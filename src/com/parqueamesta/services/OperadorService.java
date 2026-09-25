package com.parqueamesta.services;

import com.parqueamesta.model.Operador;
import com.parqueamesta.persistence.OperadorRepository;

import java.util.List;
import java.util.Optional;

public class OperadorService {
    private final OperadorRepository repo = new OperadorRepository();

    public boolean save(String nombre, String cedula) {
        if (nombreInvalido(nombre)) return false;
        if (cedulaInvalida(cedula)) return false;

        var o = new Operador(nombre, cedula);
        return repo.save(o);
    }

    public Optional<Operador> findByCedula(String cedula) {
        if (cedulaInvalida(cedula)) return Optional.empty();

        return repo.get(cedula);
    }

    public List<Operador> findAll() {
        return repo.getAll();
    }

    private boolean nombreInvalido(String nombre) {
        return nombre == null || nombre.isBlank() || nombre.length() < 6;
    }

    private boolean cedulaInvalida(String cedula) {
        return cedula == null || !cedula.matches("^[1-9][0-9]{7}([0-9]{2})?");
    }
}
