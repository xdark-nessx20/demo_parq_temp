package com.parqueamestapp.persistence;

import com.parqueamestapp.model.TipoVehiculo;
import com.parqueamestapp.persistence.utils.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record TipoVehiculoRepository() {

    private static final String COLUMNAS = "id, nombre, formato_placa";

    private TipoVehiculo construir(ResultSet result) throws SQLException {
        return new TipoVehiculo(
                result.getObject("id", UUID.class),
                result.getString("nombre"),
                result.getString("formato_placa"));
    }

    public boolean save(TipoVehiculo tipoVehiculo) {
        var query = "INSERT INTO tipos_vehiculo (nombre, formato_placa) VALUES (?, ?)";
        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);

            statement.setString(1, tipoVehiculo.nombre());
            statement.setString(2, tipoVehiculo.formatoPlaca());

            int affectedRows = statement.executeUpdate();
            statement.close();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<TipoVehiculo> get(String nombre) {
        var query = "SELECT " + COLUMNAS + " FROM tipos_vehiculo WHERE LOWER(nombre) LIKE LOWER(?)";

        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setString(1, "%" + nombre + "%");

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(construir(result));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<TipoVehiculo> getById(UUID id) {
        var query = "SELECT " + COLUMNAS + " FROM tipos_vehiculo WHERE id = ?";
        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setObject(1, id);
            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(construir(result));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TipoVehiculo> getAll() {
        var query = "SELECT " + COLUMNAS + " FROM tipos_vehiculo";
        var tipos = new ArrayList<TipoVehiculo>();

        try (Connection connection = DB.conectar(); var statement = connection.prepareStatement(query);
             var result = statement.executeQuery()) {
            while (result.next()) {
                tipos.add(construir(result));
            }
            return tipos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(UUID id, String nombre, String formatoPlaca) {
        var query = "UPDATE tipos_vehiculo SET nombre = ?, formato_placa = ? WHERE id = ?";
        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setString(1, nombre);
            statement.setString(2, formatoPlaca);
            statement.setObject(3, id);
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
