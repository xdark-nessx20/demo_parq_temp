package com.parqueamesta.services;

import com.parqueamesta.persistence.utils.DB;
import com.parqueamesta.services.exceptions.TicketAbiertoException;
import com.parqueamesta.services.exceptions.TicketNoEncontradoException;
import com.parqueamesta.services.exceptions.TicketYaCerradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistroIngresoServiceTest {

    private final RegistroIngresoService service = new RegistroIngresoService();
    private final TarifaService tarifaService = new TarifaService();

    @BeforeEach
    void limpiarTablas() throws SQLException {
        try (var connection = DB.conectar(); var statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM pago");
            statement.executeUpdate("DELETE FROM registro_ingreso");
            statement.executeUpdate("DELETE FROM tarifa");
            statement.executeUpdate("DELETE FROM vehiculos");
            statement.executeUpdate("DELETE FROM clientes");
            statement.executeUpdate("DELETE FROM tipos_vehiculo");
        }
    }

    // Crea un tipo, cliente, vehiculo y tarifa de prueba y devuelve sus ids.
    private Fixture crearVehiculoConTarifa(BigDecimal valorHora) throws SQLException {
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
                    "INSERT INTO tarifa (id_tipo_vehiculo, valor_hora, anio_vigencia) VALUES (?, ?, ?)")) {
                st.setObject(1, idTipo);
                st.setBigDecimal(2, valorHora);
                st.setInt(3, Year.now().getValue());
                st.executeUpdate();
            }

            return new Fixture(idVehiculo, idTipo);
        }
    }

    private record Fixture(UUID idVehiculo, UUID idTipoVehiculo) {
    }

    @Test
    void registrarIngreso_devuelveTicketConId() throws SQLException {
        var fixture = crearVehiculoConTarifa(new BigDecimal("2000.00"));
        var hora = LocalDateTime.of(2026, 9, 24, 8, 0);

        var ticket = service.registrarIngreso(fixture.idVehiculo(), hora, UUID.randomUUID());

        assertTrue(ticket.isPresent());
        assertNotNull(ticket.get().id());
        assertEquals(fixture.idVehiculo(), ticket.get().idVehiculo());
        assertEquals(hora, ticket.get().horaEntrada());
    }

    @Test
    void registrarIngreso_rechazaDobleIngreso() throws SQLException {
        var fixture = crearVehiculoConTarifa(new BigDecimal("2000.00"));

        var primero = service.registrarIngreso(fixture.idVehiculo(), LocalDateTime.now(), UUID.randomUUID());

        assertTrue(primero.isPresent());
        assertThrows(TicketAbiertoException.class, () ->
                service.registrarIngreso(fixture.idVehiculo(), LocalDateTime.now(), UUID.randomUUID()));
    }

    @Test
    void registrarSalida_generaPagoCorrecto() throws SQLException {
        var fixture = crearVehiculoConTarifa(new BigDecimal("2000.00"));
        var entrada = LocalDateTime.of(2026, 9, 24, 8, 0);
        var salida = entrada.plusHours(2);

        var ticket = service.registrarIngreso(fixture.idVehiculo(), entrada, UUID.randomUUID());
        assertTrue(ticket.isPresent());

        var pago = service.registrarSalida(ticket.get().id(), salida, UUID.randomUUID(), fixture.idTipoVehiculo());

        assertTrue(pago.isPresent());
        assertEquals(new BigDecimal("4000.00"), pago.get().valor());
        assertEquals(ticket.get().id(), pago.get().idRegistroIngreso());
    }

    @Test
    void registrarSalida_rechazaTicketYaCerrado() throws SQLException {
        var fixture = crearVehiculoConTarifa(new BigDecimal("2000.00"));
        var entrada = LocalDateTime.of(2026, 9, 24, 8, 0);

        var ticket = service.registrarIngreso(fixture.idVehiculo(), entrada, UUID.randomUUID());
        assertTrue(ticket.isPresent());

        var primero = service.registrarSalida(ticket.get().id(), entrada.plusHours(1), UUID.randomUUID(),
                fixture.idTipoVehiculo());

        assertTrue(primero.isPresent());
        assertThrows(TicketYaCerradoException.class, () ->
                service.registrarSalida(ticket.get().id(), entrada.plusHours(2), UUID.randomUUID(),
                        fixture.idTipoVehiculo()));
    }

    @Test
    void registrarIngreso_conNullDevuelveVacio() throws SQLException {
        var vacio = service.registrarIngreso(null, LocalDateTime.now(), UUID.randomUUID());
        assertTrue(vacio.isEmpty());
    }

    @Test
    void registrarSalida_conTicketInexistenteLanzaExcepcion() {
        assertThrows(TicketNoEncontradoException.class, () ->
                service.registrarSalida(UUID.randomUUID(), LocalDateTime.now(), UUID.randomUUID(),
                        UUID.randomUUID()));
    }

    @Test
    void historialVehiculo_listaTickets() throws SQLException {
        var fixture = crearVehiculoConTarifa(new BigDecimal("2000.00"));
        var hora = LocalDateTime.of(2026, 9, 24, 8, 0);

        service.registrarIngreso(fixture.idVehiculo(), hora, UUID.randomUUID());

        var historial = service.historialVehiculo(fixture.idVehiculo());
        assertEquals(1, historial.size());
    }

    @Test
    void registrarSalida_revierteTicketSiFallaElPago() throws SQLException {
        // Tarifa maxima (NUMERIC 10,2): al cobrar 2 horas = 199999999.98, desborda la columna
        var fixture = crearVehiculoConTarifa(new BigDecimal("99999999.99"));
        var entrada = LocalDateTime.of(2026, 9, 24, 8, 0);

        var ticket = service.registrarIngreso(fixture.idVehiculo(), entrada, UUID.randomUUID());
        assertTrue(ticket.isPresent());

        // El INSERT del pago falla (overflow numerico) -> SQLException -> RuntimeException
        assertThrows(RuntimeException.class, () ->
                service.registrarSalida(ticket.get().id(), entrada.plusHours(2), UUID.randomUUID(),
                        fixture.idTipoVehiculo()));

        // La transaccion se revirtio: el ticket sigue abierto (hora_salida = null)
        var ticketAbierto = service.ticketActivo(fixture.idVehiculo());
        assertTrue(ticketAbierto.isPresent());
    }
}