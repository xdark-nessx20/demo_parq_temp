package com.parqueamesta.services;

import com.parqueamesta.model.TipoVehiculo;
import com.parqueamesta.persistence.TipoVehiculoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TipoVehiculoService {
    private final TipoVehiculoRepository repo =  new TipoVehiculoRepository();

    public TipoVehiculoService() {
    }

    public boolean save(String nombre) {
        if (nombreInvalido(nombre)) return false;

        return repo.save(new TipoVehiculo(nombre));
    }

    public Optional<TipoVehiculo> findByName(String nombre) {
        if (nombreInvalido(nombre)) return Optional.empty();
        return repo.get(nombre);
    }

    public Optional<TipoVehiculo> findById(UUID id) {
        if (id == null) return Optional.empty();
        return repo.getById(id);
    }

    public List<TipoVehiculo> findAll() {
        return repo.getAll();
    }

    public boolean update(UUID id, String nombre) {
        if (id == null) return false;
        if (nombreInvalido(nombre)) return false;
        return repo.update(id, nombre);
    }

    public boolean delete(UUID id) {
        if (id == null) return false;
        return repo.delete(id);
    }

    private boolean nombreInvalido(String nombre) {
        return nombre == null || nombre.isBlank() || nombre.length() < 3;
    }
}
