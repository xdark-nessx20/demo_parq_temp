package com.parqueamesta.persistence;

import com.parqueamesta.model.Gerente;
import com.parqueamesta.persistence.utils.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record GerenteRepository() {
    public boolean save(Gerente gerente) {
        var query = "INSERT INTO gerentes (nombre, cedula) VALUES (?, ?)";

        try (Connection connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setString(1, gerente.nombre());
            statement.setString(2, gerente.cedula());

            int affectedRows = statement.executeUpdate();
            statement.close();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Gerente> get(String cedula) {
        var query = "SELECT * FROM gerentes WHERE cedula = ?";

        try (Connection connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setString(1, cedula);

            var result = statement.executeQuery();
            if (result.next()) {
                var id = result.getObject("id", UUID.class);
                var nombre = result.getString("nombre");

                return Optional.of(new Gerente(id, nombre, cedula));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Gerente> getAll() {
        var query = "SELECT * FROM gerentes";
        var gerentes = new ArrayList<Gerente>();

        try (Connection connection = DB.conectar(); var statment = connection.prepareStatement(query);
             var set = statment.executeQuery()) {

            while (set.next()) {
                var id = set.getObject("id", UUID.class);
                var nombre = set.getString("nombre");
                var cedula = set.getString("cedula");
                gerentes.add(new Gerente(id, nombre, cedula));
            }
            return gerentes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
