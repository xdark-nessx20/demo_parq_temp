package com.parqueamesta.services;

import com.parqueamesta.model.Tarifa;
import com.parqueamesta.persistence.RepositorioTarifa;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TarifaService {
    private final RepositorioTarifa repo = new RepositorioTarifa();

    public TarifaService() {
    }

    public boolean save(UUID idTipoVehiculo, BigDecimal valorHora, int anioVigencia) {
        if (idTipoVehiculo == null || valorHora == null || valorHora.signum() <= 0) return false;
        return repo.save(new Tarifa(idTipoVehiculo, valorHora, anioVigencia));
    }

    // Devuelve la tarifa vigente (año actual) para un tipo de vehiculo.
    public Optional<Tarifa> tarifaActual(UUID idTipoVehiculo) {
        if (idTipoVehiculo == null) return Optional.empty();
        return repo.getByTipoYAnio(idTipoVehiculo, Year.now().getValue());
    }

    // Por si el gerente actualiza el precio por hora de una tarifa.
    public boolean actualizarPrecio(UUID idTarifa, BigDecimal nuevoValor) {
        if (idTarifa == null || nuevoValor == null || nuevoValor.signum() <= 0) return false;
        return repo.updateValorHora(idTarifa, nuevoValor);
    }

    public List<Tarifa> listar() {
        return repo.getAll();
    }

    // Calcula el valor a pagar por el tiempo de estadia.
    // Politica: hora completa, cualquier fraccion se redondea hacia arriba, minimo 1 hora.
    public BigDecimal calcular(BigDecimal valorHora, LocalDateTime horaEntrada, LocalDateTime horaSalida) {
        long horas = horasCobradas(horaEntrada, horaSalida);
        return valorHora.multiply(BigDecimal.valueOf(horas)).setScale(2, RoundingMode.HALF_UP);
    }

    private long horasCobradas(LocalDateTime horaEntrada, LocalDateTime horaSalida) {
        long minutos = Duration.between(horaEntrada, horaSalida).toMinutes();
        long horas = (long) Math.ceil(minutos / 60.0);
        return Math.max(horas, 1);
    }
}