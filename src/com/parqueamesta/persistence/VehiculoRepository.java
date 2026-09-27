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
                SELECT v.id, v.marca, 
                       u.id as own_id, u.nombre as own_nombre, u.cedula as own_cedula, 
                       t.id as ty_id, t.nombre as ty_nombre
                FROM vehiculos v
                JOIN usuarios u ON v.owner_id = u.id 
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
                    var owner_cedula = result.getString("own_cedula");
                    var owner = new Cliente(owner_id, owner_name, owner_cedula);

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

    public Optional<Vehiculo> getById(UUID id) {
        var query = """
                SELECT v.id, v.placa, v.marca, 
                       u.id as own_id, u.nombre as own_nombre, u.cedula as own_cedula, 
                       t.id as ty_id, t.nombre as ty_nombre
                FROM vehiculos v
                JOIN usuarios u ON v.owner_id = u.id 
                JOIN tipos_vehiculo t ON v.tipo_id = t.id
                WHERE v.id = ?
                """;

        try (Connection connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setObject(1, id);

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    var vid = result.getObject("id", UUID.class);
                    var placa = result.getString("placa");
                    var marca = result.getString("marca");

                    var owner_id = result.getObject("own_id", UUID.class);
                    var owner_name = result.getString("own_nombre");
                    var owner_cedula = result.getString("own_cedula");
                    var owner = new Cliente(owner_id, owner_name, owner_cedula);

                    var tipo_id = result.getObject("ty_id", UUID.class);
                    var tipo_name = result.getString("ty_nombre");
                    var tipo = new TipoVehiculo(tipo_id, tipo_name, null);

                    return Optional.of(new Vehiculo(vid, placa, marca, owner, tipo));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Vehiculo> getAll() {
        var query = """
                SELECT v.id, v.placa, v.marca, 
                       u.id as own_id, u.nombre as own_nombre, u.cedula as own_cedula, 
                       t.id as ty_id, t.nombre as ty_nombre
                FROM vehiculos v
                LEFT JOIN usuarios u ON v.owner_id = u.id 
                LEFT JOIN tipos_vehiculo t ON v.tipo_id = t.id
                """;
        var vehiculos = new ArrayList<Vehiculo>();

        try (Connection connection = DB.conectar(); var statement = connection.prepareStatement(query);
             var set = statement.executeQuery()) {
            while (set.next()) {
                var id = set.getObject("id", UUID.class);
                var placa = set.getString("placa");
                var marca = set.getString("marca");

                Cliente owner = null;
                var ownerId = set.getObject("own_id", UUID.class);
                if (ownerId != null) {
                    owner = new Cliente(ownerId, set.getString("own_nombre"), set.getString("own_cedula"));
                }

                TipoVehiculo tipo = null;
                var tipoId = set.getObject("ty_id", UUID.class);
                if (tipoId != null) {
                    tipo = new TipoVehiculo(tipoId, set.getString("ty_nombre"), null);
                }

                vehiculos.add(new Vehiculo(id, placa, marca, owner, tipo));
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
