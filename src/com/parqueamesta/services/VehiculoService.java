package com.parqueamesta.services;

import com.parqueamesta.model.Cliente;
import com.parqueamesta.model.Rol;
import com.parqueamesta.model.Vehiculo;
import com.parqueamesta.persistence.TipoVehiculoRepository;
import com.parqueamesta.persistence.UsuarioRepository;
import com.parqueamesta.persistence.VehiculoRepository;

import java.util.List;
import java.util.Optional;

public class VehiculoService {
    private final VehiculoRepository repo = new VehiculoRepository();
    private final TipoVehiculoRepository tipoRepo = new TipoVehiculoRepository();
    private final UsuarioRepository usuarioRepo = new UsuarioRepository();

    public VehiculoService() {
    }

    public boolean save(String placa, String marca, String ownerCedula, String nombreTipo) {
        if (placaInvalida(placa)) return false;
        if (marcaInvalida(marca)) return false;
        if (ownerCedulaInvalido(ownerCedula)) return false;
        if (tipoInvalido(nombreTipo)) return false;

        var t = tipoRepo.get(nombreTipo);
        if (t.isEmpty()) return false;

        var o = usuarioRepo.get(ownerCedula);
        if (o.isEmpty() || o.get().rol() != Rol.CLIENTE) return false;

        var v = new Vehiculo(placa, marca, (Cliente) o.get(), t.get());
        return repo.save(v);
    }

    public Optional<Vehiculo> findByPlaca(String placa) {
        if (placaInvalida(placa)) return Optional.empty();

        return repo.get(placa);
    }

    public List<Vehiculo> findAll(){
        return repo.getAll();
    }

    public boolean delete(String placa){
        if (placaInvalida(placa)) return false;

        return repo.delete(placa);
    }

    private boolean placaInvalida(String placa) {
        return placa == null || !placa.matches("[A-Z]{3}-[0-9]{3}");
    }

    private boolean marcaInvalida(String marca) {
        return marca == null || marca.isBlank();
    }

    private boolean ownerCedulaInvalido(String ownerCedula) {
        return ownerCedula == null || !ownerCedula.matches("^[1-9][0-9]{7}([0-9]{2})?");
    }

    private boolean tipoInvalido(String tipo) {
        return tipo == null || tipo.isBlank();
    }
}
