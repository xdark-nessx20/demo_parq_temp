package com.parqueamesta.services;

import com.parqueamesta.model.Pago;
import com.parqueamesta.model.RegistroIngreso;
import com.parqueamesta.persistence.RepositorioPago;
import com.parqueamesta.persistence.RepositorioRegistroIngreso;
import com.parqueamesta.persistence.utils.DB;
import com.parqueamesta.services.exceptions.TarifaNoEncontradaException;
import com.parqueamesta.services.exceptions.TicketAbiertoException;
import com.parqueamesta.services.exceptions.TicketNoEncontradoException;
import com.parqueamesta.services.exceptions.TicketYaCerradoException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RegistroIngresoService {
    private final RepositorioRegistroIngreso repo = new RepositorioRegistroIngreso();
    private final RepositorioPago pagoRepo = new RepositorioPago();
    private final TarifaService tarifaService = new TarifaService();
    private final PagoService pagoService = new PagoService();

    public RegistroIngresoService() {
    }

    // Registra la entrada de un vehiculo. Falla si el vehiculo ya tiene un ticket abierto.
    public Optional<RegistroIngreso> registrarIngreso(UUID idVehiculo, LocalDateTime horaEntrada,
                                                      UUID idOperadorEntrada) {
        if (idVehiculo == null || horaEntrada == null || idOperadorEntrada == null) return Optional.empty();
        if (repo.getActiveByVehiculo(idVehiculo).isPresent()) {
            throw new TicketAbiertoException();
        }

        return repo.save(new RegistroIngreso(idVehiculo, horaEntrada, idOperadorEntrada));
    }

    // Registra la salida: cierra el ticket, calcula el valor y genera el pago.
    // Todo ocurre en una sola transaccion: si falla algo, se revierte (rollback).
    // Recibe idTipoVehiculo porque el VehiculoRepository aun no tiene getById(id).
    // TODO: cuando Ivan o Luis termine, ahi si uso Vehiculo (vehiculo.tipo().id()).
    public Optional<Pago> registrarSalida(UUID idRegistroIngreso, LocalDateTime horaSalida,
                                          UUID idOperadorSalida, UUID idTipoVehiculo) {
        if (idRegistroIngreso == null || horaSalida == null || idTipoVehiculo == null) return Optional.empty();

        var registroOpt = repo.get(idRegistroIngreso);
        if (registroOpt.isEmpty()) {
            throw new TicketNoEncontradoException();
        }

        var registro = registroOpt.get();
        if (registro.horaSalida() != null) {
            throw new TicketYaCerradoException();
        }

        var tarifaOpt = tarifaService.tarifaActual(idTipoVehiculo);
        if (tarifaOpt.isEmpty()) {
            throw new TarifaNoEncontradaException();
        }

        var valor = tarifaService.calcular(tarifaOpt.get().valorHora(), registro.horaEntrada(), horaSalida);
        //here a transaction, if not then rollback everything
        try (var connection = DB.conectar()) {
            connection.setAutoCommit(false);
            try {
                if (!repo.setSalida(connection, idRegistroIngreso, horaSalida, idOperadorSalida)) {
                    connection.rollback();
                    return Optional.empty();
                }
                if (!pagoRepo.save(connection, new Pago(idRegistroIngreso, valor, horaSalida))) {
                    connection.rollback();
                    return Optional.empty();
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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