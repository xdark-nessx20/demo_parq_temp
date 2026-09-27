package com.parqueamestapp.model;

import java.util.UUID;

// Fila del portal del cliente: un movimiento (ticket en curso, pago pendiente o pago hecho).
public class MovimientoCliente {
    private final String placa;
    private final String tipo;
    private final String horaEntrada;
    private final String horaSalida;
    private final String valor;
    private final String estado;
    private final UUID idPago;

    public MovimientoCliente(String placa, String tipo, String horaEntrada, String horaSalida,
                             String valor, String estado, UUID idPago) {
        this.placa = placa;
        this.tipo = tipo;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.valor = valor;
        this.estado = estado;
        this.idPago = idPago;
    }

    public String getPlaca() {
        return placa;
    }

    public String getTipo() {
        return tipo;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public String getValor() {
        return valor;
    }

    public String getEstado() {
        return estado;
    }

    public UUID getIdPago() {
        return idPago;
    }

    // Solo se puede pagar si hay un pago pendiente.
    public boolean isPagable() {
        return idPago != null && "Pendiente".equals(estado);
    }
}
