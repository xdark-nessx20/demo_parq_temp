package com.parqueamesta.services;

import com.parqueamesta.model.Cliente;
import com.parqueamesta.model.Rol;
import com.parqueamesta.model.Vehiculo;
import com.parqueamesta.persistence.TipoVehiculoRepository;
import com.parqueamesta.persistence.UsuarioRepository;
import com.parqueamesta.persistence.VehiculoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VehiculoService {
    private final VehiculoRepository repo = new VehiculoRepository();
    private final TipoVehiculoRepository tipoRepo = new TipoVehiculoRepository();
    private final UsuarioRepository usuarioRepo = new UsuarioRepository();

    public VehiculoService() {
    }

    // Todo vehiculo debe tener dueno (cliente): no se permiten vehiculos sin propietario.
    public boolean save(String placa, String ownerCedula, String nombreTipo) {
        if (tipoInvalido(nombreTipo)) return false;
        if (!placaValida(nombreTipo, placa)) return false;
        if (ownerCedula == null || ownerCedula.isBlank()) return false;

        var t = tipoRepo.get(nombreTipo);
        if (t.isEmpty()) return false;

        if (ownerCedulaInvalido(ownerCedula)) return false;
        var o = usuarioRepo.get(ownerCedula);
        if (o.isEmpty() || o.get().rol() != Rol.CLIENTE) return false;

        return repo.save(new Vehiculo(normalizarPlaca(placa), (Cliente) o.get(), t.get()));
    }

    public Optional<Vehiculo> findByPlaca(String placa) {
        if (placa == null || placa.isBlank()) return Optional.empty();

        return repo.get(normalizarPlaca(placa));
    }

    public Optional<Vehiculo> findById(UUID id) {
        if (id == null) return Optional.empty();

        return repo.getById(id);
    }

    public List<Vehiculo> findAll(){
        return repo.getAll();
    }

    public List<Vehiculo> findByOwner(UUID ownerId) {
        if (ownerId == null) return List.of();
        return repo.getByOwner(ownerId);
    }

    public boolean delete(String placa){
        if (placa == null || placa.isBlank()) return false;

        return repo.delete(placa);
    }

    public boolean updateTipo(String placa, String nombreTipo) {
        if (placa == null || placa.isBlank() || nombreTipo == null || nombreTipo.isBlank()) return false;
        var t = tipoRepo.get(nombreTipo);
        if (t.isEmpty()) return false;
        return repo.updateTipo(placa, t.get().id());
    }

    public boolean existePlaca(String placa) {
        return placa != null && repo.get(normalizarPlaca(placa)).isPresent();
    }

    // Valida el formato de placa segun el tipo (Carro: ABC-123 · Moto: ABC-12A).
    public boolean placaValidaPara(String nombreTipo, String placa) {
        return placaValida(nombreTipo, placa);
    }

    // Acepta con o sin guion (VJI43C o VJI-43C) y normaliza a "VJI-43C".
    private String normalizarPlaca(String placa) {
        if (placa == null) return null;
        var p = placa.toUpperCase().replaceAll("[^A-Z0-9]", "");
        return p.length() >= 4 ? p.substring(0, 3) + "-" + p.substring(3) : p;
    }

    private boolean placaValida(String nombreTipo, String placa) {
        if (placa == null || nombreTipo == null) return false;
        var p = normalizarPlaca(placa);
        if (nombreTipo.toLowerCase().contains("moto")) {
            return p.matches("^[A-Z]{3}-[0-9]{2}[A-Z]$");
        }
        return p.matches("^[A-Z]{3}-[0-9]{3}$");
    }

    private boolean ownerCedulaInvalido(String ownerCedula) {
        return !ownerCedula.matches("^[1-9][0-9]{7,9}$");
    }

    private boolean tipoInvalido(String tipo) {
        return tipo == null || tipo.isBlank();
    }
}
