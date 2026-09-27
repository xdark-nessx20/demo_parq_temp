package com.parqueamesta.persistence;

import com.parqueamesta.model.Cliente;
import com.parqueamesta.persistence.utils.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record ClienteRepository() {
    public boolean save(Cliente cliente) {
        var query = "INSERT INTO clientes (nombre, cedula) VALUES (?, ?)";

        try (Connection connection = DB.conectar()){
            var statement = connection.prepareStatement(query);
            statement.setString(1, cliente.nombre());
            statement.setString(2, cliente.cedula());

            int affectedRows = statement.executeUpdate();
            statement.close();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Cliente> get(String cedula) {
        var query = "SELECT * FROM clientes WHERE cedula = ?";

        try (Connection connection = DB.conectar()){
            var statement = connection.prepareStatement(query);
            statement.setString(1, cedula);

            var result =  statement.executeQuery();
            if (result.next()) {
                var id = result.getObject("id", UUID.class);
                var nombre = result.getString("nombre");

                return Optional.of(new Cliente(id, nombre, cedula));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Cliente> getAll() {
        var query = "SELECT * FROM clientes";
        var clientes = new ArrayList<Cliente>();

        try (Connection connection = DB.conectar(); var statment = connection.prepareStatement(query);
            var set = statment.executeQuery()) {

            while (set.next()) {
                var id = set.getObject("id", UUID.class);
                var nombre = set.getString("nombre");
                var cedula = set.getString("cedula");
                clientes.add(new Cliente(id, nombre, cedula));
            }
            return clientes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
