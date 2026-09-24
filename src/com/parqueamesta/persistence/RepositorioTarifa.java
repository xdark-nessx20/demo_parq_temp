package com.parqueamesta.persistence;

import com.parqueamesta.model.Tarifa;
import com.parqueamesta.persistence.utils.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record RepositorioTarifa() {

    public boolean save(Tarifa tarifa) {
        var query = "INSERT INTO tarifa (id, id_tipo_vehiculo, valor_hora, anio_vigencia) VALUES (?, ?, ?, ?)";
        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);

            statement.setObject(1, tarifa.id());
            statement.setObject(2, tarifa.idTipoVehiculo());
            statement.setDouble(3, tarifa.valorHora());
            statement.setInt(4, tarifa.anioVigencia());

            int affectedRows = statement.executeUpdate();
            statement.close();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Tarifa> getAll() {
        var query = "SELECT id, id_tipo_vehiculo, valor_hora, anio_vigencia FROM tarifa";
        var tarifas = new ArrayList<Tarifa>();

        try (Connection connection = DB.conectar(); var statement = connection.prepareStatement(query);
             var result = statement.executeQuery()) {
            while (result.next()) {
                var id = result.getObject("id", UUID.class);
                var idTipoVehiculo = result.getObject("id_tipo_vehiculo", UUID.class);
                var valorHora = result.getDouble("valor_hora");
                var anioVigencia = result.getInt("anio_vigencia");
                tarifas.add(new Tarifa(id, idTipoVehiculo, valorHora, anioVigencia));
            }
            return tarifas;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Tarifa> getByTipoYAnio(UUID idTipoVehiculo, int anio) {
        var query = "SELECT id, id_tipo_vehiculo, valor_hora, anio_vigencia FROM tarifa " +
                    "WHERE id_tipo_vehiculo = ? AND anio_vigencia = ?";

        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setObject(1, idTipoVehiculo);
            statement.setInt(2, anio);

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    var id = result.getObject("id", UUID.class);
                    var tipo = result.getObject("id_tipo_vehiculo", UUID.class);
                    var valorHora = result.getDouble("valor_hora");
                    var anioVigencia = result.getInt("anio_vigencia");
                    return Optional.of(new Tarifa(id, tipo, valorHora, anioVigencia));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}