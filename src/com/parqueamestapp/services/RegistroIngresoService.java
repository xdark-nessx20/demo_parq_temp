package com.parqueamestapp.services;

import com.parqueamestapp.model.Pago;
import com.parqueamestapp.model.RegistroIngreso;
import com.parqueamestapp.persistence.RepositorioPago;
import com.parqueamestapp.persistence.RepositorioRegistroIngreso;
import com.parqueamestapp.persistence.VehiculoRepository;
import com.parqueamestapp.persistence.utils.DB;
import com.parqueamestapp.services.exceptions.TarifaNoEncontradaException;
import com.parqueamestapp.services.exceptions.TicketAbiertoException;
import com.parqueamestapp.services.exceptions.TicketNoEncontradoException;
import com.parqueamestapp.services.exceptions.TicketYaCerradoException;
import com.parqueamestapp.services.exceptions.VehiculoNoEncontradoException;

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
    private final VehiculoRepository vehiculoRepo = new VehiculoRepository();

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
    // El tipo de vehiculo se obtiene del propio vehiculo asociado al ticket.
    public Optional<Pago> registrarSalida(UUID idRegistroIngreso, LocalDateTime horaSalida,
                                          UUID idOperadorSalida) {
        if (idRegistroIngreso == null || horaSalida == null) return Optional.empty();

        var registroOpt = repo.get(idRegistroIngreso);
        if (registroOpt.isEmpty()) {
            throw new TicketNoEncontradoException();
        }

        var registro = registroOpt.get();
        if (registro.horaSalida() != null) {
            throw new TicketYaCerradoException();
        }

        var vehiculo = vehiculoRepo.getById(registro.idVehiculo())
                .orElseThrow(VehiculoNoEncontradoException::new);
        var idTipoVehiculo = vehiculo.tipo().id();

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

    // Todos los tickets abiertos (para elegir en la pantalla de salida).
    public List<RegistroIngreso> ticketsActivos() {
        return repo.getActivos();
    }

    // Busca un ticket por id.
    public Optional<RegistroIngreso> buscar(UUID id) {
        if (id == null) return Optional.empty();
        return repo.get(id);
    }

    public List<RegistroIngreso> listar() {
        return repo.getAll();
    }

    public List<RegistroIngreso> historialVehiculo(UUID idVehiculo) {
        return repo.getByVehiculo(idVehiculo);
    }
}