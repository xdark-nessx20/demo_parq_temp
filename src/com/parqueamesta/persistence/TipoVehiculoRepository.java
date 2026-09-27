package com.parqueamesta.persistence;

import com.parqueamesta.model.TipoVehiculo;
import com.parqueamesta.persistence.utils.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record TipoVehiculoRepository() {

    public boolean save(TipoVehiculo tipoVehiculo) {
        var query = "INSERT INTO tipos_vehiculo (nombre) VALUES (?)";
        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);

            statement.setString(1, tipoVehiculo.nombre());

            int affectedRows = statement.executeUpdate();
            statement.close();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<TipoVehiculo> get(String nombre) {
        var query = "SELECT id, nombre FROM tipos_vehiculo WHERE LOWER(nombre) LIKE LOWER(?)";

        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setString(1, "%" + nombre + "%");

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    UUID id = result.getObject("id", UUID.class);
                    return Optional.of(new TipoVehiculo(id, result.getString("nombre")));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<TipoVehiculo> getById(UUID id) {
        var query = "SELECT id, nombre FROM tipos_vehiculo WHERE id = ?";
        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setObject(1, id);
            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(new TipoVehiculo(id, result.getString("nombre")));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TipoVehiculo> getAll() {
        var query = "SELECT id, nombre FROM tipos_vehiculo";
        var tipos = new ArrayList<TipoVehiculo>();

        try (Connection connection = DB.conectar(); var statement = connection.prepareStatement(query);
             var result = statement.executeQuery()) {
            while (result.next()) {
                tipos.add(new TipoVehiculo(result.getObject("id", UUID.class), result.getString("nombre")));
            }
            return tipos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(UUID id, String nombre) {
        var query = "UPDATE tipos_vehiculo SET nombre = ? WHERE id = ?";
        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setString(1, nombre);
            statement.setObject(2, id);
            int affectedRows = statement.executeUpdate();
            statement.close();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(UUID id) {
        var query = "DELETE FROM tipos_vehiculo WHERE id = ?";
        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setObject(1, id);
            int affectedRows = statement.executeUpdate();
            statement.close();
            return affectedRows > 0;
        } catch (SQLException e) {
            return false; // p.ej. el tipo tiene tarifas o vehiculos asociados (FK)
        }
    }
}
