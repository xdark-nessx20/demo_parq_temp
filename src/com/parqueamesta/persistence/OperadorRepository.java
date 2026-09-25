package com.parqueamesta.persistence;

import com.parqueamesta.model.Operador;
import com.parqueamesta.persistence.utils.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record OperadorRepository() {
    public boolean save(Operador operador) {
        var query = "INSERT INTO operadores (nombre, cedula) VALUES (?, ?)";

        try (Connection connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setString(1, operador.nombre());
            statement.setString(2, operador.cedula());

            int affectedRows = statement.executeUpdate();
            statement.close();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Operador> get(String cedula) {
        var query = "SELECT * FROM operadores WHERE cedula = ?";

        try (Connection connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setString(1, cedula);

            var result = statement.executeQuery();
            if (result.next()) {
                var id = result.getObject("id", UUID.class);
                var nombre = result.getString("nombre");

                return Optional.of(new Operador(id, nombre, cedula));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Operador> getAll() {
        var query = "SELECT * FROM operadores";
        var operadores = new ArrayList<Operador>();

        try (Connection connection = DB.conectar(); var statment = connection.prepareStatement(query);
             var set = statment.executeQuery()) {

            while (set.next()) {
                var id = set.getObject("id", UUID.class);
                var nombre = set.getString("nombre");
                var cedula = set.getString("cedula");
                operadores.add(new Operador(id, nombre, cedula));
            }
            return operadores;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
