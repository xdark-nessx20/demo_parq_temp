package com.parqueamesta.services;

import com.parqueamesta.model.TipoVehiculo;
import com.parqueamesta.persistence.TipoVehiculoRepository;

import java.util.List;
import java.util.Optional;

public class TipoVehiculoService {
    private final TipoVehiculoRepository repo =  new TipoVehiculoRepository();

    public TipoVehiculoService() {
    }

    public boolean save(String nombre, String descripcion) {
        if (nombreInvalido(nombre)) return false;

        var tipo = new TipoVehiculo(nombre, descripcion);
        return repo.save(tipo);
    }

    public Optional<TipoVehiculo> findByName(String nombre) {
        if (nombreInvalido(nombre)) return Optional.empty();
        return repo.get(nombre);
    }

    public List<TipoVehiculo> findAll() {
        return repo.getAll();
    }

    private boolean nombreInvalido(String nombre) {
        return nombre == null || nombre.isBlank() || nombre.length() < 5;
    }
}
