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

    public Optional<TipoVehiculo> get(UUID id) {
        var query = "SELECT * FROM tipos_vehiculo WHERE id = ?";

        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setObject(1, id);

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    UUID _id = result.getObject("id", UUID.class);
                    var nombre = result.getString("nombre");
                    var descripcion = result.getString("descripcion");
                    return Optional.of(new TipoVehiculo(_id, nombre, descripcion));
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
