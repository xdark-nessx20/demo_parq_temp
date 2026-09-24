package com.parqueamesta.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Pago {
    private UUID id;
    private UUID idRegistroIngreso;
    private BigDecimal valor;
    private LocalDateTime fechaPago;

    public Pago(UUID id, UUID idRegistroIngreso, BigDecimal valor, LocalDateTime fechaPago) {
        this.id = id;
        this.idRegistroIngreso = idRegistroIngreso;
        this.valor = valor;
        this.fechaPago = fechaPago;
    }

    // Constructor para crear un pago nuevo (el id lo genera Postgres)
    public Pago(UUID idRegistroIngreso, BigDecimal valor, LocalDateTime fechaPago) {
        this.idRegistroIngreso = idRegistroIngreso;
        this.valor = valor;
        this.fechaPago = fechaPago;
    }

    // Getters con record style
    public UUID id() {
        return id;
    }

    public UUID idRegistroIngreso() {
        return idRegistroIngreso;
    }

    public BigDecimal valor() {
        return valor;
    }

    public LocalDateTime fechaPago() {
        return fechaPago;
    }

    // Setters
    public void setIdRegistroIngreso(UUID idRegistroIngreso) {
        this.idRegistroIngreso = idRegistroIngreso;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pago pago)) return false;
        return Objects.equals(id, pago.id) && Objects.equals(idRegistroIngreso, pago.idRegistroIngreso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idRegistroIngreso);
    }

    @Override
    public String toString() {
        return "Pago {id: %s, idRegistroIngreso: %s, valor: %s, fechaPago: %s}"
                .formatted(id, idRegistroIngreso, valor, fechaPago);
    }
}