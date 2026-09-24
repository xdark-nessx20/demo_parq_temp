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

    public boolean save(Pago pago) {
        var query = "INSERT INTO pago (id, id_registro_ingreso, valor, hora_pago) VALUES (?, ?, ?, ?)";
        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);

            statement.setObject(1, pago.id());
            statement.setObject(2, pago.idRegistroIngreso());
            statement.setDouble(3, pago.valor());
            statement.setTimestamp(4, Timestamp.valueOf(pago.horaPago()));

            int affectedRows = statement.executeUpdate();
            statement.close();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pago> getAll() {
        var query = "SELECT id, id_registro_ingreso, valor, hora_pago FROM pago";
        var pagos = new ArrayList<Pago>();

        try (Connection connection = DB.conectar(); var statement = connection.prepareStatement(query);
             var result = statement.executeQuery()) {
            while (result.next()) {
                var id = result.getObject("id", UUID.class);
                var idRegistroIngreso = result.getObject("id_registro_ingreso", UUID.class);
                var valor = result.getDouble("valor");
                var horaPago = result.getTimestamp("hora_pago").toLocalDateTime();
                pagos.add(new Pago(id, idRegistroIngreso, valor, horaPago));
            }
            return pagos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Pago> get(UUID id) {
        var query = "SELECT id, id_registro_ingreso, valor, hora_pago FROM pago WHERE id = ?";

        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setObject(1, id);

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    var idRegistroIngreso = result.getObject("id_registro_ingreso", UUID.class);
                    var valor = result.getDouble("valor");
                    var horaPago = result.getTimestamp("hora_pago").toLocalDateTime();
                    return Optional.of(new Pago(id, idRegistroIngreso, valor, horaPago));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Busca el pago de un registro de ingreso (el pago de un ticket específico)
    public Optional<Pago> getByRegistroIngreso(UUID idRegistroIngreso) {
        var query = "SELECT id, id_registro_ingreso, valor, hora_pago FROM pago WHERE id_registro_ingreso = ?";

        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setObject(1, idRegistroIngreso);

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    var id = result.getObject("id", UUID.class);
                    var valor = result.getDouble("valor");
                    var horaPago = result.getTimestamp("hora_pago").toLocalDateTime();
                    return Optional.of(new Pago(id, idRegistroIngreso, valor, horaPago));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}