package com.parqueamesta.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class RegistroIngreso {
    private UUID id;
    private UUID idVehiculo;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;
    private UUID idOperadorEntrada;
    private UUID idOperadorSalida;

    public RegistroIngreso(UUID id, UUID idVehiculo, LocalDateTime horaEntrada, LocalDateTime horaSalida,
                           UUID idOperadorEntrada, UUID idOperadorSalida) {
        this.id = id;
        this.idVehiculo = idVehiculo;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.idOperadorEntrada = idOperadorEntrada;
        this.idOperadorSalida = idOperadorSalida;
    }

    // Constructor para crear un ingreso nuevo (el id lo genera Postgres)
    public RegistroIngreso(UUID idVehiculo, LocalDateTime horaEntrada, UUID idOperadorEntrada) {
        this.idVehiculo = idVehiculo;
        this.horaEntrada = horaEntrada;
        this.idOperadorEntrada = idOperadorEntrada;
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

    public UUID idOperadorEntrada() {
        return idOperadorEntrada;
    }

    public UUID idOperadorSalida() {
        return idOperadorSalida;
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

    public void setIdOperadorEntrada(UUID idOperadorEntrada) {
        this.idOperadorEntrada = idOperadorEntrada;
    }

    public void setIdOperadorSalida(UUID idOperadorSalida) {
        this.idOperadorSalida = idOperadorSalida;
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
        return "RegistroIngreso {id: %s, idVehiculo: %s, horaEntrada: %s, horaSalida: %s, " +
                "idOperadorEntrada: %s, idOperadorSalida: %s}"
                .formatted(id, idVehiculo, horaEntrada, horaSalida, idOperadorEntrada, idOperadorSalida);
    }
}