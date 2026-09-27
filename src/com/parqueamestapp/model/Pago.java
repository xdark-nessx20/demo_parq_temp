package com.parqueamestapp.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Pago {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private UUID id;
    private UUID idRegistroIngreso;
    private BigDecimal valor;
    private LocalDateTime fechaPago;
    private boolean pagado;

    public Pago(UUID id, UUID idRegistroIngreso, BigDecimal valor, LocalDateTime fechaPago, boolean pagado) {
        this.id = id;
        this.idRegistroIngreso = idRegistroIngreso;
        this.valor = valor;
        this.fechaPago = fechaPago;
        this.pagado = pagado;
    }

    // Constructor para crear un pago nuevo (nace pendiente de pago)
    public Pago(UUID idRegistroIngreso, BigDecimal valor, LocalDateTime fechaPago) {
        this.idRegistroIngreso = idRegistroIngreso;
        this.valor = valor;
        this.fechaPago = fechaPago;
        this.pagado = false;
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

    public boolean pagado() {
        return pagado;
    }

    // Getters JavaBean (para JSP/EL)
    public UUID getId() {
        return id;
    }

    public UUID getIdRegistroIngreso() {
        return idRegistroIngreso;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public boolean isPagado() {
        return pagado;
    }

    public boolean getPagado() {
        return pagado;
    }

    // Fecha formateada para mostrar en pantalla (dd/MM/yyyy HH:mm)
    public String getFechaPagoTexto() {
        return fechaPago == null ? "" : fechaPago.format(FORMATO);
    }

    // Valor con separador de miles (p.ej. 1.800,00)
    public String getValorTexto() {
        return com.parqueamestapp.util.Formato.moneda(valor);
    }

    public String getEstado() {
        return pagado ? "Pagado" : "Pendiente";
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

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
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
        return "Pago {id: %s, idRegistroIngreso: %s, valor: %s, fechaPago: %s, pagado: %s}"
                .formatted(id, idRegistroIngreso, valor, fechaPago, pagado);
    }
}
