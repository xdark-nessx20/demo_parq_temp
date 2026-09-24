package com.parqueamesta.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class RegistroIngreso {
    private UUID id;
    private UUID idVehiculo;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;

    public RegistroIngreso(UUID id, UUID idVehiculo, LocalDateTime horaEntrada, LocalDateTime horaSalida) {
        this.id = id;
        this.idVehiculo = idVehiculo;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    public RegistroIngreso(UUID idVehiculo, LocalDateTime horaEntrada) {
        this.idVehiculo = idVehiculo;
        this.horaEntrada = horaEntrada;
    }

    // Getters con record style
    public UUID id() {
        return id;
    }

    public UUID idVehiculo() {
        return idVehiculo;
    }

    public LocalDateTime horaEntrada() {
        return horaEntrada;
    }

    public LocalDateTime horaSalida() {
        return horaSalida;
    }

    // Setters
    public void setIdVehiculo(UUID idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public void setHoraEntrada(LocalDateTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public void setHoraSalida(LocalDateTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RegistroIngreso that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(idVehiculo, that.idVehiculo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idVehiculo);
    }

    @Override
    public String toString() {
        return "RegistroIngreso {id: %s, idVehiculo: %s, horaEntrada: %s, horaSalida: %s}"
                .formatted(id, idVehiculo, horaEntrada, horaSalida);
    }
}