package com.parqueamesta.services;

import com.parqueamesta.persistence.utils.DB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TarifaServiceTest {
    private final TarifaService tarifaService = new TarifaService();
    private final BigDecimal valorHora = new BigDecimal("2000.00");

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

    // Crea un tipo de vehiculo y su tarifa y devuelve el id del tipo.
    private UUID crearTipoConTarifa(BigDecimal valorHora) throws SQLException {
        var sufijo = UUID.randomUUID().toString().substring(0, 8);
        try (var connection = DB.conectar()) {
            UUID idTipo;
            try (var st = connection.prepareStatement(
                    "INSERT INTO tipos_vehiculo (nombre, descripcion) VALUES (?, ?) RETURNING id")) {
                st.setString(1, "Tipo-" + sufijo);
                st.setString(2, "desc");
                try (var rs = st.executeQuery()) { rs.next(); idTipo = rs.getObject("id", UUID.class); }
            }

            try (var st = connection.prepareStatement(
                    "INSERT INTO tarifa (id_tipo_vehiculo, valor_hora, anio_vigencia) VALUES (?, ?, ?)")) {
                st.setObject(1, idTipo);
                st.setBigDecimal(2, valorHora);
                st.setInt(3, Year.now().getValue());
                st.executeUpdate();
            }
            return idTipo;
        }
    }

    // ---- Tests de calculo (logica pura) ----

    private LocalDateTime entrada() {
        return LocalDateTime.of(2026, 9, 24, 8, 0);
    }

    @Test
    void calcular_cobraUnaHoraExacta() {
        var salida = entrada().plusHours(1);
        assertEquals(new BigDecimal("2000.00"), tarifaService.calcular(valorHora, entrada(), salida));
    }

    @Test
    void calcular_redondeaFraccionHaciaArriba() {
        var salida = entrada().plusHours(1).plusMinutes(35);
        assertEquals(new BigDecimal("4000.00"), tarifaService.calcular(valorHora, entrada(), salida));
    }

    @Test
    void calcular_cobraMinimoUnaHora() {
        var salida = entrada().plusMinutes(5);
        assertEquals(new BigDecimal("2000.00"), tarifaService.calcular(valorHora, entrada(), salida));
    }

    @Test
    void calcular_cobraTresHorasExactas() {
        var salida = entrada().plusHours(3);
        assertEquals(new BigDecimal("6000.00"), tarifaService.calcular(valorHora, entrada(), salida));
    }

    @Test
    void calcular_redondeaHaciaArribaConMediaHoraExtra() {
        var salida = entrada().plusHours(2).plusMinutes(30);
        assertEquals(new BigDecimal("6000.00"), tarifaService.calcular(valorHora, entrada(), salida));
    }

    @Test
    void calcular_redondeaResultadoADosDecimales() {
        var salida = entrada().plusHours(1);
        var resultado = tarifaService.calcular(new BigDecimal("1500.505"), entrada(), salida);
        assertEquals(new BigDecimal("1500.51"), resultado);
    }

    // ---- Tests con BD ----

    @Test
    void tarifaActual_devuelveTarifaDelAnio() throws SQLException {
        var idTipo = crearTipoConTarifa(new BigDecimal("2000.00"));

        var tarifa = tarifaService.tarifaActual(idTipo);

        assertTrue(tarifa.isPresent());
        assertEquals(new BigDecimal("2000.00"), tarifa.get().valorHora());
        assertEquals(Year.now().getValue(), tarifa.get().anioVigencia());
    }

    @Test
    void actualizarPrecio_cambiaTarifaActual() throws SQLException {
        var idTipo = crearTipoConTarifa(new BigDecimal("2000.00"));

        var tarifa = tarifaService.tarifaActual(idTipo);
        assertTrue(tarifa.isPresent());

        boolean ok = tarifaService.actualizarPrecio(tarifa.get().id(), new BigDecimal("3000.00"));

        assertTrue(ok);
        var tarifaActualizada = tarifaService.tarifaActual(idTipo);
        assertTrue(tarifaActualizada.isPresent());
        assertEquals(new BigDecimal("3000.00"), tarifaActualizada.get().valorHora());
    }

    @Test
    void actualizarPrecio_rechazaValorNoPositivo() throws SQLException {
        var idTipo = crearTipoConTarifa(new BigDecimal("2000.00"));

        var tarifa = tarifaService.tarifaActual(idTipo);
        assertTrue(tarifa.isPresent());

        assertTrue(!tarifaService.actualizarPrecio(tarifa.get().id(), new BigDecimal("0")));
        assertTrue(!tarifaService.actualizarPrecio(tarifa.get().id(), null));
    }
}