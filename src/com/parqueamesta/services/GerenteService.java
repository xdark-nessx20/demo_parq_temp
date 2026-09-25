package com.parqueamesta.services;

import com.parqueamesta.model.Gerente;
import com.parqueamesta.persistence.GerenteRepository;

import java.util.List;
import java.util.Optional;

public class GerenteService {
    private final GerenteRepository repo = new GerenteRepository();

    public boolean save(String nombre, String cedula) {
        if (nombreInvalido(nombre)) return false;
        if (cedulaInvalida(cedula)) return false;

        var g = new Gerente(nombre, cedula);
        return repo.save(g);
    }

    public Optional<Gerente> findByCedula(String cedula) {
        if (cedulaInvalida(cedula)) return Optional.empty();

        return repo.get(cedula);
    }

    public List<Gerente> findAll() {
        return repo.getAll();
    }

    private boolean nombreInvalido(String nombre) {
        return nombre == null || nombre.isBlank() || nombre.length() < 6;
    }

    private boolean cedulaInvalida(String cedula) {
        return cedula == null || !cedula.matches("^[1-9][0-9]{7}([0-9]{2})?");
    }
}
