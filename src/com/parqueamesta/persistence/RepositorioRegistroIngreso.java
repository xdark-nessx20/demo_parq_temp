package com.parqueamesta.persistence;

import com.parqueamesta.model.RegistroIngreso;
import com.parqueamesta.persistence.utils.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record RepositorioRegistroIngreso() {

    public Optional<UUID> save(RegistroIngreso registro) {
        var query = "INSERT INTO registro_ingreso (id_vehiculo, hora_entrada, hora_salida, " +
                    "id_operador_entrada, id_operador_salida) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);

            statement.setObject(1, registro.idVehiculo());
            statement.setTimestamp(2, Timestamp.valueOf(registro.horaEntrada()));
            statement.setObject(3, registro.horaSalida() != null ? Timestamp.valueOf(registro.horaSalida()) : null);
            statement.setObject(4, registro.idOperadorEntrada());
            statement.setObject(5, registro.idOperadorSalida() != null ? registro.idOperadorSalida() : null);

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(result.getObject("id", UUID.class));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<RegistroIngreso> getAll() {
        var query = "SELECT id, id_vehiculo, hora_entrada, hora_salida, " +
                    "id_operador_entrada, id_operador_salida FROM registro_ingreso";
        var registros = new ArrayList<RegistroIngreso>();

        try (Connection connection = DB.conectar(); var statement = connection.prepareStatement(query);
             var result = statement.executeQuery()) {
            while (result.next()) {
                var id = result.getObject("id", UUID.class);
                var idVehiculo = result.getObject("id_vehiculo", UUID.class);
                var horaEntrada = result.getTimestamp("hora_entrada").toLocalDateTime();
                var tsSalida = result.getTimestamp("hora_salida");
                var horaSalida = tsSalida != null ? tsSalida.toLocalDateTime() : null;
                var idOperadorEntrada = result.getObject("id_operador_entrada", UUID.class);
                var idOperadorSalida = result.getObject("id_operador_salida", UUID.class);
                registros.add(new RegistroIngreso(id, idVehiculo, horaEntrada, horaSalida,
                        idOperadorEntrada, idOperadorSalida));
            }
            return registros;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<RegistroIngreso> get(UUID id) {
        var query = "SELECT id, id_vehiculo, hora_entrada, hora_salida, " +
                    "id_operador_entrada, id_operador_salida FROM registro_ingreso WHERE id = ?";

        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setObject(1, id);

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    var idVehiculo = result.getObject("id_vehiculo", UUID.class);
                    var horaEntrada = result.getTimestamp("hora_entrada").toLocalDateTime();
                    var tsSalida = result.getTimestamp("hora_salida");
                    var horaSalida = tsSalida != null ? tsSalida.toLocalDateTime() : null;
                    var idOperadorEntrada = result.getObject("id_operador_entrada", UUID.class);
                    var idOperadorSalida = result.getObject("id_operador_salida", UUID.class);
                    return Optional.of(new RegistroIngreso(id, idVehiculo, horaEntrada, horaSalida,
                            idOperadorEntrada, idOperadorSalida));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Historial completo de un vehículo (todas sus entradas y salidas)
    public List<RegistroIngreso> getByVehiculo(UUID idVehiculo) {
        var query = "SELECT id, id_vehiculo, hora_entrada, hora_salida, " +
                    "id_operador_entrada, id_operador_salida FROM registro_ingreso " +
                    "WHERE id_vehiculo = ? ORDER BY hora_entrada DESC";
        var registros = new ArrayList<RegistroIngreso>();

        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setObject(1, idVehiculo);

            try (var result = statement.executeQuery()) {
                while (result.next()) {
                    var id = result.getObject("id", UUID.class);
                    var horaEntrada = result.getTimestamp("hora_entrada").toLocalDateTime();
                    var tsSalida = result.getTimestamp("hora_salida");
                    var horaSalida = tsSalida != null ? tsSalida.toLocalDateTime() : null;
                    var idOperadorEntrada = result.getObject("id_operador_entrada", UUID.class);
                    var idOperadorSalida = result.getObject("id_operador_salida", UUID.class);
                    registros.add(new RegistroIngreso(id, idVehiculo, horaEntrada, horaSalida,
                            idOperadorEntrada, idOperadorSalida));
                }
            }
            return registros;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Busca el ticket abierto de un vehículo (el que todavía no ha salido)
    public Optional<RegistroIngreso> getActiveByVehiculo(UUID idVehiculo) {
        var query = "SELECT id, id_vehiculo, hora_entrada, hora_salida, " +
                    "id_operador_entrada, id_operador_salida FROM registro_ingreso " +
                    "WHERE id_vehiculo = ? AND hora_salida IS NULL";

        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setObject(1, idVehiculo);

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    var id = result.getObject("id", UUID.class);
                    var horaEntrada = result.getTimestamp("hora_entrada").toLocalDateTime();
                    var idOperadorEntrada = result.getObject("id_operador_entrada", UUID.class);
                    var idOperadorSalida = result.getObject("id_operador_salida", UUID.class);
                    return Optional.of(new RegistroIngreso(id, idVehiculo, horaEntrada, null,
                            idOperadorEntrada, idOperadorSalida));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Pone la hora de salida y el operador de salida al ticket cuando el vehículo sale
    public boolean setSalida(UUID id, LocalDateTime horaSalida, UUID idOperadorSalida) {
        var query = "UPDATE registro_ingreso SET hora_salida = ?, id_operador_salida = ? WHERE id = ?";
        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);

            statement.setTimestamp(1, Timestamp.valueOf(horaSalida));
            statement.setObject(2, idOperadorSalida);
            statement.setObject(3, id);

            int affectedRows = statement.executeUpdate();
            statement.close();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}