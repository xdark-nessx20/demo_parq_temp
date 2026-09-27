package com.parqueamestapp.services;

import com.parqueamestapp.model.TipoVehiculo;
import com.parqueamestapp.persistence.TipoVehiculoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TipoVehiculoService {
    private final TipoVehiculoRepository repo =  new TipoVehiculoRepository();

    public TipoVehiculoService() {
    }

    // Solo hay dos formatos de placa posibles.
    public static final String FORMATO_CARRO = "CARRO";
    public static final String FORMATO_MOTO = "MOTO";

    public boolean save(String nombre, String formatoPlaca) {
        if (nombreInvalido(nombre)) return false;
        if (formatoInvalido(formatoPlaca)) return false;

        return repo.save(new TipoVehiculo(nombre, formatoPlaca.toUpperCase()));
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

    public boolean update(UUID id, String nombre, String formatoPlaca) {
        if (id == null) return false;
        if (nombreInvalido(nombre)) return false;
        if (formatoInvalido(formatoPlaca)) return false;
        return repo.update(id, nombre, formatoPlaca.toUpperCase());
    }

    public boolean delete(UUID id) {
        if (id == null) return false;
        return repo.delete(id);
    }

    private boolean nombreInvalido(String nombre) {
        return nombre == null || nombre.isBlank() || nombre.length() < 3;
    }

    private boolean formatoInvalido(String formato) {
        return !FORMATO_CARRO.equalsIgnoreCase(formato) && !FORMATO_MOTO.equalsIgnoreCase(formato);
    }
}
