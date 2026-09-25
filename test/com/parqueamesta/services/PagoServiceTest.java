package com.parqueamesta.services;

import com.parqueamesta.persistence.utils.DB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PagoServiceTest {

    private final PagoService pagoService = new PagoService();

    @BeforeEach
    void limpiarTablas() throws SQLException {
        try (var connection = DB.conectar(); var statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM pago");
            statement.executeUpdate("DELETE FROM registro_ingreso");
            statement.executeUpdate("DELETE FROM vehiculos");
            statement.executeUpdate("DELETE FROM clientes");
            statement.executeUpdate("DELETE FROM tipos_vehiculo");
        }
    }

    // Crea un vehiculo real (con cliente y tipo) y su registro_ingreso de prueba.
    private UUID crearRegistro() throws SQLException {
        var sufijo = UUID.randomUUID().toString().substring(0, 8);
        try (var connection = DB.conectar()) {

            UUID idTipo;
            try (var st = connection.prepareStatement(
                    "INSERT INTO tipos_vehiculo (nombre, descripcion) VALUES (?, ?) RETURNING id")) {
                st.setString(1, "Tipo-" + sufijo);
                st.setString(2, "desc");
                try (var rs = st.executeQuery()) { rs.next(); idTipo = rs.getObject("id", UUID.class); }
            }

            UUID idCliente;
            try (var st = connection.prepareStatement(
                    "INSERT INTO clientes (nombre, cedula) VALUES (?, ?) RETURNING id")) {
                st.setString(1, "Cliente Test");
                st.setString(2, sufijo);
                try (var rs = st.executeQuery()) { rs.next(); idCliente = rs.getObject("id", UUID.class); }
            }

            UUID idVehiculo;
            try (var st = connection.prepareStatement(
                    "INSERT INTO vehiculos (placa, marca, owner_id, tipo_id) VALUES (?, ?, ?, ?) RETURNING id")) {
                st.setString(1, "ABC-" + sufijo.substring(0, 3));
                st.setString(2, "Marca");
                st.setObject(3, idCliente);
                st.setObject(4, idTipo);
                try (var rs = st.executeQuery()) { rs.next(); idVehiculo = rs.getObject("id", UUID.class); }
            }

            try (var st = connection.prepareStatement(
                    "INSERT INTO registro_ingreso (id_vehiculo, hora_entrada, hora_salida, " +
                    "id_operador_entrada, id_operador_salida) VALUES (?, ?, NULL, ?, NULL) RETURNING id")) {
                st.setObject(1, idVehiculo);
                st.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                st.setObject(3, UUID.randomUUID());
                try (var rs = st.executeQuery()) { rs.next(); return rs.getObject("id", UUID.class); }
            }
        }
    }

    @Test
    void guardar_y_buscarPorRegistro() throws SQLException {
        var idRegistro = crearRegistro();
        var fecha = LocalDateTime.of(2026, 9, 24, 10, 30);

        boolean ok = pagoService.guardar(idRegistro, new BigDecimal("4000.00"), fecha);

        assertTrue(ok);
        var pago = pagoService.buscarPorRegistro(idRegistro);
        assertTrue(pago.isPresent());
        assertEquals(new BigDecimal("4000.00"), pago.get().valor());
        assertEquals(fecha, pago.get().fechaPago());
    }

    @Test
    void guardar_rechazaValorNoPositivo() throws SQLException {
        var idRegistro = crearRegistro();

        assertTrue(!pagoService.guardar(idRegistro, new BigDecimal("0"), LocalDateTime.now()));
        assertTrue(!pagoService.guardar(idRegistro, null, LocalDateTime.now()));
    }
}