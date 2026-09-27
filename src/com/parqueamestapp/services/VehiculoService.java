package com.parqueamestapp.services;

import com.parqueamestapp.model.Cliente;
import com.parqueamestapp.model.Rol;
import com.parqueamestapp.model.TipoVehiculo;
import com.parqueamestapp.model.Vehiculo;
import com.parqueamestapp.persistence.TipoVehiculoRepository;
import com.parqueamestapp.persistence.UsuarioRepository;
import com.parqueamestapp.persistence.VehiculoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VehiculoService {
    private final VehiculoRepository repo = new VehiculoRepository();
    private final TipoVehiculoRepository tipoRepo = new TipoVehiculoRepository();
    private final UsuarioRepository usuarioRepo = new UsuarioRepository();

    public VehiculoService() {
    }

    // Resultado de "reclamar" un vehiculo sin dueno desde la app del cliente.
    public enum ResultadoReclamo { OK, NO_EXISTE, YA_TIENE_DUENO, YA_ES_TUYO }

    // El dueno es OPCIONAL: un vehiculo puede entrar al parqueadero sin dueno
    // (cliente nuevo) y luego el cliente lo reclama desde la app.
    public boolean save(String placa, String ownerCedula, String nombreTipo) {
        if (tipoInvalido(nombreTipo)) return false;

        var t = tipoRepo.get(nombreTipo);
        if (t.isEmpty()) return false;
        if (!placaValida(t.get(), placa)) return false;

        Cliente owner = null;
        if (ownerCedula != null && !ownerCedula.isBlank()) {
            if (ownerCedulaInvalido(ownerCedula)) return false;
            var o = usuarioRepo.get(ownerCedula);
            if (o.isEmpty() || o.get().rol() != Rol.CLIENTE) return false;
            owner = (Cliente) o.get();
        }

        return repo.save(new Vehiculo(normalizarPlaca(placa), owner, t.get()));
    }

    // El cliente reclama un vehiculo que esta sin dueno y lo registra a su nombre.
    public ResultadoReclamo reclamar(String placa, UUID ownerId) {
        if (placa == null || placa.isBlank() || ownerId == null) return ResultadoReclamo.NO_EXISTE;

        var v = repo.get(normalizarPlaca(placa));
        if (v.isEmpty()) return ResultadoReclamo.NO_EXISTE;
        if (v.get().owner() != null) {
            return ownerId.equals(v.get().owner().id())
                    ? ResultadoReclamo.YA_ES_TUYO
                    : ResultadoReclamo.YA_TIENE_DUENO;
        }
        return repo.asignarDueno(normalizarPlaca(placa), ownerId)
                ? ResultadoReclamo.OK
                : ResultadoReclamo.NO_EXISTE;
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

    // Valida el formato de placa segun el tipo de vehiculo.
    public boolean placaValidaPara(String nombreTipo, String placa) {
        if (tipoInvalido(nombreTipo)) return false;
        return tipoRepo.get(nombreTipo).map(t -> placaValida(t, placa)).orElse(false);
    }

    // Acepta con o sin guion (VJI43C o VJI-43C) y normaliza a "VJI-43C".
    private String normalizarPlaca(String placa) {
        if (placa == null) return null;
        var p = placa.toUpperCase().replaceAll("[^A-Z0-9]", "");
        return p.length() >= 4 ? p.substring(0, 3) + "-" + p.substring(3) : p;
    }

    // El formato lo define el tipo: Carro -> AAA-000 | Moto -> AAA-00A.
    private boolean placaValida(TipoVehiculo tipo, String placa) {
        if (placa == null || tipo == null) return false;
        var p = normalizarPlaca(placa);
        if (tipo.esMoto()) {
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
