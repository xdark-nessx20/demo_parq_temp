package com.parqueamestapp.persistence;


import com.parqueamestapp.model.Cliente;
import com.parqueamestapp.model.TipoVehiculo;
import com.parqueamestapp.model.Vehiculo;
import com.parqueamestapp.persistence.utils.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record VehiculoRepository() {
    public boolean save(Vehiculo vehiculo) {
        if (get(vehiculo.placa()).isPresent()) return false; // la placa ya existe

        var query = "INSERT INTO vehiculos (placa, owner_id, tipo_id) VALUES (?, ?, ?)";
        try (Connection conn = DB.conectar()) {
            var statement = conn.prepareStatement(query);
            statement.setString(1, vehiculo.placa());
            statement.setObject(2, vehiculo.owner() != null ? vehiculo.owner().id() : null);
            statement.setObject(3, vehiculo.tipo() != null ? vehiculo.tipo().id() : null);

            int affectedRows = statement.executeUpdate();
            statement.close();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Vehiculo> get(String placa) {
        var query = """
                SELECT v.id, v.placa, 
                       u.id as own_id, u.nombre as own_nombre, u.cedula as own_cedula, 
                       t.id as ty_id, t.nombre as ty_nombre, t.formato_placa as ty_formato
                FROM vehiculos v
                LEFT JOIN usuarios u ON v.owner_id = u.id 
                JOIN tipos_vehiculo t ON v.tipo_id = t.id
                WHERE v.placa = ?
                """;

        try (Connection connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setString(1, placa.toUpperCase());

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    var id = result.getObject("id", UUID.class);

                    //Owner (opcional)
                    Cliente owner = null;
                    var owner_id = result.getObject("own_id", UUID.class);
                    if (owner_id != null) {
                        owner = new Cliente(owner_id, result.getString("own_nombre"), result.getString("own_cedula"));
                    }

                    //Tipo
                    var tipo_id = result.getObject("ty_id", UUID.class);
                    var tipo_name = result.getString("ty_nombre");
                    var tipo = new TipoVehiculo(tipo_id, tipo_name, result.getString("ty_formato"));

                    return Optional.of(new Vehiculo(id, placa, owner, tipo));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Vehiculo> getById(UUID id) {
        var query = """
                SELECT v.id, v.placa, 
                       u.id as own_id, u.nombre as own_nombre, u.cedula as own_cedula, 
                       t.id as ty_id, t.nombre as ty_nombre, t.formato_placa as ty_formato
                FROM vehiculos v
                LEFT JOIN usuarios u ON v.owner_id = u.id 
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

                    Cliente owner = null;
                    var owner_id = result.getObject("own_id", UUID.class);
                    if (owner_id != null) {
                        owner = new Cliente(owner_id, result.getString("own_nombre"), result.getString("own_cedula"));
                    }

                    var tipo_id = result.getObject("ty_id", UUID.class);
                    var tipo_name = result.getString("ty_nombre");
                    var tipo = new TipoVehiculo(tipo_id, tipo_name, result.getString("ty_formato"));

                    return Optional.of(new Vehiculo(vid, placa, owner, tipo));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Vehiculo> getAll() {
        var query = """
                SELECT v.id, v.placa, 
                       u.id as own_id, u.nombre as own_nombre, u.cedula as own_cedula, 
                       t.id as ty_id, t.nombre as ty_nombre, t.formato_placa as ty_formato
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

                Cliente owner = null;
                var ownerId = set.getObject("own_id", UUID.class);
                if (ownerId != null) {
                    owner = new Cliente(ownerId, set.getString("own_nombre"), set.getString("own_cedula"));
                }

                TipoVehiculo tipo = null;
                var tipoId = set.getObject("ty_id", UUID.class);
                if (tipoId != null) {
                    tipo = new TipoVehiculo(tipoId, set.getString("ty_nombre"), set.getString("ty_formato"));
                }

                vehiculos.add(new Vehiculo(id, placa, owner, tipo));
            }
            return vehiculos;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Vehiculo> getByOwner(UUID ownerId) {
        var query = """
                SELECT v.id, v.placa, 
                       u.id as own_id, u.nombre as own_nombre, u.cedula as own_cedula, 
                       t.id as ty_id, t.nombre as ty_nombre, t.formato_placa as ty_formato
                FROM vehiculos v
                LEFT JOIN usuarios u ON v.owner_id = u.id 
                LEFT JOIN tipos_vehiculo t ON v.tipo_id = t.id
                WHERE v.owner_id = ?
                """;
        var vehiculos = new ArrayList<Vehiculo>();

        try (Connection connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setObject(1, ownerId);
            try (var set = statement.executeQuery()) {
                while (set.next()) {
                    var id = set.getObject("id", UUID.class);
                    var placa = set.getString("placa");

                    Cliente owner = null;
                    var ownerIdR = set.getObject("own_id", UUID.class);
                    if (ownerIdR != null) {
                        owner = new Cliente(ownerIdR, set.getString("own_nombre"), set.getString("own_cedula"));
                    }

                    TipoVehiculo tipo = null;
                    var tipoId = set.getObject("ty_id", UUID.class);
                    if (tipoId != null) {
                        tipo = new TipoVehiculo(tipoId, set.getString("ty_nombre"), set.getString("ty_formato"));
                    }

                    vehiculos.add(new Vehiculo(id, placa, owner, tipo));
                }
            }
            return vehiculos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateTipo(String placa, UUID tipoId) {
        var query = "UPDATE vehiculos SET tipo_id = ? WHERE placa = ?";
        try (Connection connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setObject(1, tipoId);
            statement.setString(2, placa.toUpperCase());
            int affectedRows = statement.executeUpdate();
            statement.close();
            return affectedRows > 0;
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
            return false; // p.ej. el vehiculo tiene tickets asociados (FK)
        }
    }

}
