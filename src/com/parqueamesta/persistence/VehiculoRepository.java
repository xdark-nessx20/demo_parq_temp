package com.parqueamesta.persistence;


import com.parqueamesta.model.Vehiculo;
import com.parqueamesta.persistence.utils.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        var query = "SELECT * FROM vehiculos WHERE placa = ?";

        try (Connection connection = DB.conectar()){
            var statement = connection.prepareStatement(query);
            statement.setString(1, placa.toUpperCase());

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    var id = result.getObject("id", UUID.class);
                    var marca = result.getString("marca");
                    //Owner y Tipo
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
