package com.parqueamesta.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Tarifa {
    private UUID id;
    private UUID idTipoVehiculo;
    private BigDecimal valorHora;
    private int anioVigencia;

    public Tarifa(UUID id, UUID idTipoVehiculo, BigDecimal valorHora, int anioVigencia) {
        this.id = id;
        this.idTipoVehiculo = idTipoVehiculo;
        this.valorHora = valorHora;
        this.anioVigencia = anioVigencia;
    }

    // Constructor para crear una tarifa nueva (el id lo genera Postgres)
    public Tarifa(UUID idTipoVehiculo, BigDecimal valorHora, int anioVigencia) {
        this.idTipoVehiculo = idTipoVehiculo;
        this.valorHora = valorHora;
        this.anioVigencia = anioVigencia;
    }

    // Getters con record style
    public UUID id() {
        return id;
    }

    public UUID idTipoVehiculo() {
        return idTipoVehiculo;
    }

    public BigDecimal valorHora() {
        return valorHora;
    }

    public int anioVigencia() {
        return anioVigencia;
    }

    // Setters
    public void setIdTipoVehiculo(UUID idTipoVehiculo) {
        this.idTipoVehiculo = idTipoVehiculo;
    }

    public void setValorHora(BigDecimal valorHora) {
        this.valorHora = valorHora;
    }

    public void setAnioVigencia(int anioVigencia) {
        this.anioVigencia = anioVigencia;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tarifa tarifa)) return false;
        return Objects.equals(id, tarifa.id) && Objects.equals(idTipoVehiculo, tarifa.idTipoVehiculo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idTipoVehiculo);
    }

    @Override
    public String toString() {
        return "Tarifa {id: %s, idTipoVehiculo: %s, valorHora: %s, anioVigencia: %d}"
                .formatted(id, idTipoVehiculo, valorHora, anioVigencia);
    }
}