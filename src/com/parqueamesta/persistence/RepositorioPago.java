package com.parqueamesta.persistence;

import com.parqueamesta.model.Pago;
import com.parqueamesta.persistence.utils.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record RepositorioPago() {

    // Version que reutiliza la conexion pasada (para participar en una transaccion).
    public boolean save(Connection connection, Pago pago) throws SQLException {
        var query = "INSERT INTO pago (id_registro_ingreso, valor, fecha_pago) VALUES (?, ?, ?)";
        var statement = connection.prepareStatement(query);

        statement.setObject(1, pago.idRegistroIngreso());
        statement.setBigDecimal(2, pago.valor());
        statement.setTimestamp(3, Timestamp.valueOf(pago.fechaPago()));

        int affectedRows = statement.executeUpdate();
        statement.close();
        return affectedRows > 0;
    }

    // Version que abre su propia conexion (uso simple, sin transaccion).
    public boolean save(Pago pago) {
        try (var connection = DB.conectar()) {
            return save(connection, pago);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pago> getAll() {
        var query = "SELECT id, id_registro_ingreso, valor, fecha_pago FROM pago";
        var pagos = new ArrayList<Pago>();

        try (Connection connection = DB.conectar(); var statement = connection.prepareStatement(query);
             var result = statement.executeQuery()) {
            while (result.next()) {
                var id = result.getObject("id", UUID.class);
                var idRegistroIngreso = result.getObject("id_registro_ingreso", UUID.class);
                var valor = result.getBigDecimal("valor");
                var fechaPago = result.getTimestamp("fecha_pago").toLocalDateTime();
                pagos.add(new Pago(id, idRegistroIngreso, valor, fechaPago));
            }
            return pagos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Pago> get(UUID id) {
        var query = "SELECT id, id_registro_ingreso, valor, fecha_pago FROM pago WHERE id = ?";

        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setObject(1, id);

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    var idRegistroIngreso = result.getObject("id_registro_ingreso", UUID.class);
                    var valor = result.getBigDecimal("valor");
                    var fechaPago = result.getTimestamp("fecha_pago").toLocalDateTime();
                    return Optional.of(new Pago(id, idRegistroIngreso, valor, fechaPago));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Busca el pago de un registro de ingreso (el pago de un ticket específico)
    public Optional<Pago> getByRegistroIngreso(UUID idRegistroIngreso) {
        var query = "SELECT id, id_registro_ingreso, valor, fecha_pago FROM pago WHERE id_registro_ingreso = ?";

        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setObject(1, idRegistroIngreso);

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    var id = result.getObject("id", UUID.class);
                    var valor = result.getBigDecimal("valor");
                    var fechaPago = result.getTimestamp("fecha_pago").toLocalDateTime();
                    return Optional.of(new Pago(id, idRegistroIngreso, valor, fechaPago));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}