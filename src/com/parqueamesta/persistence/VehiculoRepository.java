package com.parqueamesta.persistence;


import com.parqueamesta.model.Cliente;
import com.parqueamesta.model.TipoVehiculo;
import com.parqueamesta.model.Vehiculo;
import com.parqueamesta.persistence.utils.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record VehiculoRepository() {
    public boolean save(Vehiculo vehiculo) {
        var query = "INSERT INTO vehiculos (placa, marca, owner_id, tipo_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DB.conectar()) {
            var statement = conn.prepareStatement(query);
            statement.setString(1, vehiculo.placa());
            statement.setString(2, vehiculo.marca());
            statement.setObject(3, vehiculo.owner().id());
            statement.setObject(4, vehiculo.tipo().id());

            int affectedRows = statement.executeUpdate();
            statement.close();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Vehiculo> get(String placa) {
        var query = """
                SELECT v.id, v.marca, u.id as own_id, u.nombre as own_nombre, 
                       t.id as ty_id, t.nombre as ty_nombre
                FROM vehiculos v
                JOIN users u ON v.owner_id = u.id 
                JOIN tipos_vehiculo t ON v.tipo_id = t.id
                WHERE v.placa = ?
                """;

        try (Connection connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setString(1, placa.toUpperCase());

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    var id = result.getObject("id", UUID.class);
                    var marca = result.getString("marca");

                    //Owner
                    var owner_id = result.getObject("own_id", UUID.class);
                    var owner_name = result.getString("own_nombre");
                    var owner = new Cliente(owner_id, owner_name);

                    //Tipo
                    var tipo_id = result.getObject("ty_id", UUID.class);
                    var tipo_name = result.getString("ty_nombre");
                    var tipo = new TipoVehiculo(tipo_id, tipo_name, null);

                    return Optional.of(new Vehiculo(id, placa, marca, owner, tipo));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Vehiculo> getAll() {
        var query = "SELECT id, placa, marca FROM vehiculos";
        var vehiculos = new ArrayList<Vehiculo>();

        try (Connection connection = DB.conectar(); var statement = connection.prepareStatement(query);
             var set = statement.executeQuery()) {
            while (set.next()) {
                var id = set.getObject("id", UUID.class);
                var placa = set.getString("placa");
                var marca = set.getString("marca");

                vehiculos.add(new Vehiculo(id, placa, marca, null, null));
            }
            return vehiculos;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(String placa) {
        var query = "DELETE FROM vehiculos WHERE placa = ?";

        try (Connection connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setString(1, placa.toUpperCase());

            int affectedRows = statement.executeUpdate();
            statement.close();

            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
