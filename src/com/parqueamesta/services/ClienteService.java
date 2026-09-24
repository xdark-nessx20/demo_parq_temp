package com.parqueamesta.services;

import com.parqueamesta.model.Cliente;
import com.parqueamesta.persistence.ClienteRepository;

import java.util.List;
import java.util.Optional;

public class ClienteService {
    private final ClienteRepository repo = new ClienteRepository();

    public boolean save(String nombre, String cedula) {
        if (nombreInvalido(nombre)) return false;
        if (cedulaInvalida(cedula)) return false;

        var c = new Cliente(nombre, cedula);
        return repo.save(c);
    }

    public Optional<Cliente> findByCedula(String cedula) {
        if (cedulaInvalida(cedula)) return Optional.empty();

        return repo.get(cedula);
    }

    public List<Cliente> findAll() {
        return repo.getAll();
    }

    private boolean nombreInvalido(String nombre) {
        return nombre == null || nombre.isBlank() || nombre.length() < 6;
    }

    private boolean cedulaInvalida(String cedula) {
        return cedula == null || !cedula.matches("^[1-9][0-9]{7}([0-9]{2})?");
    }
}
