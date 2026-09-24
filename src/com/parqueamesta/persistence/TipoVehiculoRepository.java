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
        var query = "INSERT INTO tipos_vehiculo (nombre, descripcion) VALUES (?, ?)";
        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);

            statement.setString(1, tipoVehiculo.nombre());
            statement.setString(2, tipoVehiculo.descripcion());

            int affectedRows = statement.executeUpdate();
            statement.close();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<TipoVehiculo> get(String nombre) {
        var query = "SELECT * FROM tipos_vehiculo WHERE LOWER(nombre) LIKE LOWER(?);";

        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setString(1, "%" + nombre + "%");

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    UUID id = result.getObject("id", UUID.class);
                    var _nombre = result.getString("_nombre");
                    var descripcion = result.getString("descripcion");
                    return Optional.of(new TipoVehiculo(id, _nombre, descripcion));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TipoVehiculo> getAll() {
        var query = "SELECT * FROM tipos_vehiculo";
        var tipos = new ArrayList<TipoVehiculo>();

        try (Connection connection = DB.conectar(); var statement = connection.prepareStatement(query);
             var result = statement.executeQuery()) {
            while (result.next()) {
                var id = result.getObject("id", UUID.class);
                var nombre = result.getString("nombre");
                var descripcion = result.getString("descripcion");
                tipos.add(new TipoVehiculo(id, nombre, descripcion));
            }
            return tipos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
