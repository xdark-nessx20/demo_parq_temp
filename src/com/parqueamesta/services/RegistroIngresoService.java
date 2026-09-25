package com.parqueamesta.services;

import com.parqueamesta.model.Pago;
import com.parqueamesta.model.RegistroIngreso;
import com.parqueamesta.persistence.RepositorioRegistroIngreso;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RegistroIngresoService {
    private final RepositorioRegistroIngreso repo = new RepositorioRegistroIngreso();
    private final TarifaService tarifaService = new TarifaService();
    private final PagoService pagoService = new PagoService();

    public RegistroIngresoService() {
    }

    // Registra la entrada de un vehiculo. Falla si el vehiculo ya tiene un ticket abierto.
    public Optional<RegistroIngreso> registrarIngreso(UUID idVehiculo, LocalDateTime horaEntrada,
                                                      UUID idOperadorEntrada) {
        if (idVehiculo == null || horaEntrada == null || idOperadorEntrada == null) return Optional.empty();
        if (repo.getActiveByVehiculo(idVehiculo).isPresent()) return Optional.empty();

        var idGenerado = repo.save(new RegistroIngreso(idVehiculo, horaEntrada, idOperadorEntrada));
        if (idGenerado.isEmpty()) return Optional.empty();

        return repo.get(idGenerado.get());
    }

    // Registra la salida: cierra el ticket, calcula el valor y genera el pago.
    // Recibe idTipoVehiculo porque el VehiculoRepository aun no tiene getById(id).
    // TODO: cuando Ivan o Luis termine, ahi si uso Vehiculo (vehiculo.tipo().id()).
    public Optional<Pago> registrarSalida(UUID idRegistroIngreso, LocalDateTime horaSalida,
                                          UUID idOperadorSalida, UUID idTipoVehiculo) {
        if (idRegistroIngreso == null || horaSalida == null || idTipoVehiculo == null) return Optional.empty();

        var registroOpt = repo.get(idRegistroIngreso);
        if (registroOpt.isEmpty()) return Optional.empty();

        var registro = registroOpt.get();
        if (registro.horaSalida() != null) return Optional.empty();

        var tarifaOpt = tarifaService.tarifaActual(idTipoVehiculo);
        if (tarifaOpt.isEmpty()) return Optional.empty();

        var valor = tarifaService.calcular(tarifaOpt.get().valorHora(), registro.horaEntrada(), horaSalida);

        if (!repo.setSalida(idRegistroIngreso, horaSalida, idOperadorSalida)) return Optional.empty();
        if (!pagoService.guardar(idRegistroIngreso, valor, horaSalida)) return Optional.empty();

        return pagoService.buscarPorRegistro(idRegistroIngreso);
    }

    // Ticket abierto de un vehiculo (el que todavia no ha salido).
    public Optional<RegistroIngreso> ticketActivo(UUID idVehiculo) {
        return repo.getActiveByVehiculo(idVehiculo);
    }

    public List<RegistroIngreso> listar() {
        return repo.getAll();
    }

    public List<RegistroIngreso> historialVehiculo(UUID idVehiculo) {
        return repo.getByVehiculo(idVehiculo);
    }
}