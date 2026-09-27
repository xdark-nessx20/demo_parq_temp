package com.parqueamesta.model;

import java.util.UUID;

// Vista de un vehiculo que esta dentro del parqueadero (para el panel de salida rapida).
public class VehiculoDentro {
    private final UUID id;
    private final String placa;
    private final String tipo;
    private final String owner;
    private final String horaEntrada;
    private final String tiempo;

    public VehiculoDentro(UUID id, String placa, String tipo, String owner, String horaEntrada, String tiempo) {
        this.id = id;
        this.placa = placa;
        this.tipo = tipo;
        this.owner = owner;
        this.horaEntrada = horaEntrada;
        this.tiempo = tiempo;
    }

    public UUID getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getTipo() {
        return tipo;
    }

    public String getOwner() {
        return owner;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public String getTiempo() {
        return tiempo;
    }
}
