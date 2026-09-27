package com.parqueamestapp.services;

import com.parqueamestapp.model.Tarifa;
import com.parqueamestapp.persistence.RepositorioTarifa;
import com.parqueamestapp.services.exceptions.TarifaDuplicadaException;

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

    // Regla: solo puede existir UNA tarifa por tipo de vehiculo y anio.
    public boolean save(UUID idTipoVehiculo, BigDecimal valorHora, int anioVigencia) {
        if (idTipoVehiculo == null || valorHora == null || valorHora.signum() <= 0) return false;
        if (repo.getByTipoYAnio(idTipoVehiculo, anioVigencia).isPresent()) {
            throw new TarifaDuplicadaException();
        }
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

    public boolean delete(UUID idTarifa) {
        if (idTarifa == null) return false;
        return repo.delete(idTarifa);
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

    // Horas que se cobran por una estadia (minimo 1, fracciones hacia arriba).
    public long horasCobradasPublico(LocalDateTime horaEntrada, LocalDateTime horaSalida) {
        return horasCobradas(horaEntrada, horaSalida);
    }
}