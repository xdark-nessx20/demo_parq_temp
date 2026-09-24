package com.parqueamesta.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Pago {
    private UUID id;
    private UUID idRegistroIngreso;
    private double valor;
    private LocalDateTime horaPago;

    public Pago(UUID id, UUID idRegistroIngreso, double valor, LocalDateTime horaPago) {
        this.id = id;
        this.idRegistroIngreso = idRegistroIngreso;
        this.valor = valor;
        this.horaPago = horaPago;
    }

    // Constructor para crear un pago nuevo (el id lo genera Postgres)
    public Pago(UUID idRegistroIngreso, double valor, LocalDateTime horaPago) {
        this.idRegistroIngreso = idRegistroIngreso;
        this.valor = valor;
        this.horaPago = horaPago;
    }

    // Getters con record style
    public UUID id() {
        return id;
    }

    public UUID idRegistroIngreso() {
        return idRegistroIngreso;
    }

    public double valor() {
        return valor;
    }

    public LocalDateTime horaPago() {
        return horaPago;
    }

    // Setters
    public void setIdRegistroIngreso(UUID idRegistroIngreso) {
        this.idRegistroIngreso = idRegistroIngreso;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setHoraPago(LocalDateTime horaPago) {
        this.horaPago = horaPago;
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
        return "Pago {id: %s, idRegistroIngreso: %s, valor: %.2f, horaPago: %s}"
                .formatted(id, idRegistroIngreso, valor, horaPago);
    }
}